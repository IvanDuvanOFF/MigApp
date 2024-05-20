import 'dart:io';

import 'package:deeplom/config/lce.dart';
import 'package:deeplom/data/models/applications_model.dart';
import 'package:deeplom/data/models/main_model.dart';
import 'package:deeplom/data/models/notification_model.dart';
import 'package:deeplom/data/models/profile_model.dart';
import 'package:flutter/foundation.dart';

abstract class AbstractMainRepository {
  Future<MainModel> getMainData();

  Future<ProfileModel> getProfileData();

  ValueListenable<Lce<ProfileModel>> get profile;

  Future<void> putFirebaseDate();

  Future<List<ApplicationsModel>> getApplications({String? date});

  Future<SelectedApplicationModel> selectApplication({required String applicationId});

  Future<List<NotificationModel>> getNotifications();

  Future<void> deleteNotification({required int notificationId});

  Future<void> logOut();

  Future<String> uploadFile({required File file});

  Future<void> addDocument({required String applicationId, required String fileName, required String title});

  Future<void> changePassword({required String oldPassword, required String newPassword});
}
