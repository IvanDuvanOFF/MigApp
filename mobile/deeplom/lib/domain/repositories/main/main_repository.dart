import 'dart:io';

import 'package:deeplom/config/api_urls.dart';
import 'package:deeplom/config/lce.dart';
import 'package:deeplom/data/dto/applications_dto.dart';
import 'package:deeplom/data/dto/main_dto.dart';
import 'package:deeplom/data/dto/notification_dto.dart';
import 'package:deeplom/data/dto/profile_dto.dart';
import 'package:deeplom/data/dto/upload_file_dto.dart';
import 'package:deeplom/data/mappers/applications_mapper.dart';
import 'package:deeplom/data/mappers/main_mapper.dart';
import 'package:deeplom/data/mappers/notification_mapper.dart';
import 'package:deeplom/data/mappers/profile_mapper.dart';
import 'package:deeplom/data/mappers/upload_file_mapper.dart';
import 'package:deeplom/data/models/applications_model.dart';
import 'package:deeplom/data/models/main_model.dart';
import 'package:deeplom/data/models/notification_model.dart';
import 'package:deeplom/data/models/profile_model.dart';
import 'package:deeplom/data/models/upload_file_model.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/firebase_options.dart';
import 'package:dio/dio.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:intl/intl.dart';
import 'package:path_provider/path_provider.dart';

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
    final selectedDate = DateFormat('yyyy-MM-dd').format(DateTime.tryParse(date ?? '') ?? DateTime.now());
    final dateUrl = '${ApiUrls.application}/$selectedDate';

    try {
      var response = await dio.get(date != null ? dateUrl : ApiUrls.application, options: options);
      var applicationData = response.data as List<dynamic>;
      var applicationModel = applicationData.map((json) => ApplicationsDto.fromJson(json).toModel()).toList();
      return applicationModel;
    } on DioException catch (e) {
      print('Error: $e');
    }
    throw Exception('ERROR');
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
  Future<void> deleteNotification({required String notificationId}) async {
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
  Future<UploadFileModel?> uploadFile({required File file}) async {
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
        final fileData = response.data as Map<String, dynamic>;
        final fileModel = UploadFileDto.fromJson(fileData).toModel();
        print('File uploaded: ${fileModel.name}');
        return fileModel;
      } else {
        print('Failed to upload file');
      }
    } on DioException catch (e) {
      print('Error: ${e.response}');
    }
    return null;
  }

  @override
  Future<DocumentModel?> addDocument({required String applicationId, required String fileName, required String title}) async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    Map<String, dynamic> data = {
      'title': title,
      'file_name': fileName,
    };
    try {
      var response = await dio.post(
        '${ApiUrls.selectApplication}/$applicationId',
        data: data,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
            'Content-Type': 'application/json',
          },
        ),
      );

      print('DOCUMENT RESPONST:: $response');
      final documentData = response.data as Map<String, dynamic>;
      final documentModel = DocumentDto.fromJson(documentData).toModel();
      return documentModel;
    } on DioException catch (e) {
      print('Error: ${e.response}');
    }
    return null;
  }

  @override
  Future<SelectedApplicationModel> selectApplication({required String applicationId}) async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    final options = Options(headers: {'Authorization': 'Bearer $token'});
    var response = await dio.get('${ApiUrls.selectApplication}/$applicationId', options: options);
    print('APPLICATION RESPONSE:: $response');
    var applicationData = response.data as Map<String, dynamic>;
    var applicationModel = SelectedApplicationDto.fromJson(applicationData).toModel();
    print('APPLICATION MODEL:: $applicationModel');
    return applicationModel;
  }

  @override
  Future<void> changePassword({required String oldPassword, required String newPassword}) async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    final options = Options(headers: {'Authorization': 'Bearer $token'});
    var data = {
      "new_password": newPassword,
      "old_password": oldPassword,
    };
    print('URL:: ${ApiUrls.profilePassword} ||  DATA PASSWORD:: $data || HEADERS:: ${options.headers}');

    try {
      await dio.patch(ApiUrls.profilePassword, data: data, options: options);
    } on DioException catch (e) {
      print(
          'Request Data: ${e.requestOptions.data} || Request Headers: ${e.requestOptions.headers} || Request path: ${e.requestOptions.path} || Request path: ${e.requestOptions.method} || Error: $e');
    }
  }

  @override
  Future<File?> getRemember() async {
    String filename = 'tutorial.pdf';
    File file;
    String dir = '';

    final options = Options(
      responseType: ResponseType.bytes,
    );
    final response = await dio.get('${ApiUrls.fileController}/$filename', options: options);

    if (Platform.isAndroid) {
      dir = '/storage/emulated/0/Download';
    } else if (Platform.isIOS) {
      dir = (await getApplicationDocumentsDirectory()).path;
    }

    file = File('$dir/$filename');

    if (response.statusCode == 200) {
      await file.writeAsBytes(response.data);
      return file;
    } else {
      return null;
    }

    // try {
    //   final response = await dio.get('${ApiUrls.fileController}/$filename', options: options);
    //   print('REMEMBER RESPONSE :: $response');
    // } on DioException catch (e, stack) {
    //   print('REMEMBER:: ${e} || $stack');
    // }
  }

  @override
  Future<void> uploadAvatar({required String name, required String link}) async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    Map<String, dynamic> data = {
      'file_name': name,
    };
    print('DATA TEST :: $data');
    try {
      var response = await dio.patch(
        ApiUrls.profilePhoto,
        data: data,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );
      if (response.statusCode == 200) {
        print('Avatar added');
      } else {
        print('Failed to add avatar');
      }
    } on DioException catch (e) {
      print('Error: ${e.response}');
    }
  }

  @override
  Future<Uint8List> getFile({required String fileName}) async {
    String? token = await _secureStorage.read(key: 'refreshToken');
    var response = await dio.get(
      '${ApiUrls.getFile}/$fileName',
      options: Options(
        headers: {'Authorization': 'Bearer $token'},
        responseType: ResponseType.bytes,
      ),
    );
    return response.data;
  }
}
