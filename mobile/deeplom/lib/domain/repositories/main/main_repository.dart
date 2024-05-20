import 'dart:io';

import 'package:deeplom/config/api_urls.dart';
import 'package:deeplom/config/lce.dart';
import 'package:deeplom/data/dto/applications_dto.dart';
import 'package:deeplom/data/dto/main_dto.dart';
import 'package:deeplom/data/dto/notification_dto.dart';
import 'package:deeplom/data/dto/profile_dto.dart';
import 'package:deeplom/data/mappers/applications_mapper.dart';
import 'package:deeplom/data/mappers/main_mapper.dart';
import 'package:deeplom/data/mappers/notification_mapper.dart';
import 'package:deeplom/data/mappers/profile_mapper.dart';
import 'package:deeplom/data/models/applications_model.dart';
import 'package:deeplom/data/models/main_model.dart';
import 'package:deeplom/data/models/notification_model.dart';
import 'package:deeplom/data/models/profile_model.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/firebase_options.dart';
import 'package:dio/dio.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:intl/intl.dart';

class MainRepository implements AbstractMainRepository {
  MainRepository({required this.dio});

  Dio dio;
  final _secureStorage = const FlutterSecureStorage();

  final ValueNotifier<Lce<ProfileModel>> _profile = ValueNotifier(const Lce.idle());
  @override
  ValueListenable<Lce<ProfileModel>> get profile => _profile;

  @override
  Future<MainModel> getMainData() async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    var response = await dio.get(ApiUrls.mainScreen, options: Options(headers: {'Authorization': 'Bearer $token'}));
    var mainData = response.data as Map<String, dynamic>;
    var mainModel = MainDto.fromJson(mainData).toModel();
    await _secureStorage.write(key: 'userID', value: mainModel.userId);
    return mainModel;
  }

  @override
  Future<List<ApplicationsModel>> getApplications({String? date}) async {
    String? token = await _secureStorage.read(key: 'refreshToken');

    final options = Options(headers: {'Authorization': 'Bearer $token'});
    final data = date != null ? {'date': DateFormat('yyyy-MM-dd').format(DateTime.parse(date))} : null;

    var response = await dio.get(ApiUrls.application, data: data, options: options);
    var applicationData = response.data as List<dynamic>;
    var applicationModel = applicationData.map((json) => ApplicationsDto.fromJson(json).toModel()).toList();
    return applicationModel;
  }

  @override
  Future<List<NotificationModel>> getNotifications() async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    var response = await dio.get(ApiUrls.notifications, options: Options(headers: {'Authorization': 'Bearer $token'}));
    var notificationData = response.data as List<dynamic>;
    var notificationModel = notificationData.map((json) => NotificationDto.fromJson(json).toModel()).toList();
    return notificationModel;
  }

  @override
  Future<void> putFirebaseDate() async {
    await Firebase.initializeApp(
      options: DefaultFirebaseOptions.currentPlatform,
    );
    FirebaseMessaging messaging = FirebaseMessaging.instance;
    NotificationSettings settings = await messaging.requestPermission(
      alert: true,
      badge: true,
      sound: true,
    );
    String? fbToken = await messaging.getToken();
    String? token = await _secureStorage.read(key: 'refreshToken');
    String? userID = await _secureStorage.read(key: 'userID');
    var data = {
      'token': fbToken,
      'user_id': userID,
    };
    await dio.put(ApiUrls.notifications, data: data, options: Options(headers: {'Authorization': 'Bearer $token'}));
    // print('TEST FB:: $fb');
  }

  @override
  Future<ProfileModel> getProfileData() async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    var response = await dio.get(ApiUrls.profile, options: Options(headers: {'Authorization': 'Bearer $token'}));
    var profileData = response.data as Map<String, dynamic>;
    var profileModel = ProfileDto.fromJson(profileData).toModel();
    _profile.value = profileModel.asContent;
    return profileModel;
  }

  @override
  Future<void> logOut() async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    await dio.post(ApiUrls.logout, options: Options(headers: {'Authorization': 'Bearer $token'}));
  }

  @override
  Future<void> deleteNotification({required int notificationId}) async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    await dio.delete(
      ApiUrls.notifications,
      data: {'notification_id': notificationId},
      options: Options(
        headers: {'Authorization': 'Bearer $token'},
      ),
    );
  }

  @override
  Future<String> uploadFile({required File file}) async {
    String fileName = '';
    String? token = await _secureStorage.read(key: 'refreshToken');
    FormData formData = FormData.fromMap({
      'file': await MultipartFile.fromFile(file.path, filename: file.path.split('/').last),
    });
    try {
      var response = await dio.post(
        ApiUrls.uploadFile,
        data: formData,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
            'Content-Type': 'application/json',
          },
        ),
      );

      if (response.statusCode == 200) {
        fileName = response.data['name'];
        print('File uploaded: $fileName');
      } else {
        print('Failed to upload file');
      }
    } on DioException catch (e) {
      print('Error: ${e.response}');
    }

    return fileName;
  }

  @override
  Future<void> addDocument({required String applicationId, required String fileName, required String title}) async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    Map<String, dynamic> data = {
      'title': title, // Замените на реальное значение
      'file_name': fileName,
    };
    try {
      var response = await dio.post(
        '${ApiUrls.application}/$applicationId',
        data: data,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
            'Content-Type': 'application/json',
          },
        ),
      );
      if (response.statusCode == 200) {
        print('Document added');
      } else {
        print('Failed to add document');
      }
    } on DioException catch (e) {
      print('Error: ${e.response}');
    }
  }

  @override
  Future<SelectedApplicationModel> selectApplication({required String applicationId}) async {
    String? token = await _secureStorage.read(key: 'refreshToken');

    final options = Options(headers: {'Authorization': 'Bearer $token'});

    var response = await dio.get('${ApiUrls.application}/$applicationId', options: options);
    var applicationData = response.data as Map<String, dynamic>;
    var applicationModel = SelectedApplicationDto.fromJson(applicationData).toModel();
    return applicationModel;
  }

  @override
  Future<void> changePassword({required String oldPassword, required String newPassword}) async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    final options = Options(headers: {'Authorization': 'Bearer $token'});
    var data = {"password": oldPassword, "confirmation": newPassword};
    await dio.patch(ApiUrls.profile, data: data, options: options);
  }
}
