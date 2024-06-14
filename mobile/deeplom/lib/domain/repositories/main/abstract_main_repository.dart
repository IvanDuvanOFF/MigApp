import 'dart:io';

import 'package:deeplom/config/lce.dart';
import 'package:deeplom/data/models/applications_model.dart';
import 'package:deeplom/data/models/main_model.dart';
import 'package:deeplom/data/models/notification_model.dart';
import 'package:deeplom/data/models/profile_model.dart';
import 'package:deeplom/data/models/upload_file_model.dart';
import 'package:flutter/foundation.dart';

abstract class AbstractMainRepository {
  Future<MainModel> getMainData();

  Future<ProfileModel> getProfileData();

  ValueListenable<Lce<ProfileModel>> get profile;

  Future<void> putFirebaseDate();

  Future<List<ApplicationsModel>> getApplications({String? date});

  Future<SelectedApplicationModel> selectApplication({required String applicationId});

  Future<List<NotificationModel>> getNotifications();

  Future<void> deleteNotification({required String notificationId});

  Future<void> logOut();

  Future<UploadFileModel?> uploadFile({required File file});

  Future<DocumentModel?> addDocument({required String applicationId, required String fileName, required String title});

  Future<void> changePassword({required String oldPassword, required String newPassword});

  Future<File?> getRemember();

  Future<void> uploadAvatar({required String name, required String link});

  Future<Uint8List> getFile({required String fileName});
}
