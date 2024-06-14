import 'dart:typed_data';

import 'package:deeplom/config/lce.dart';
import 'package:deeplom/data/models/applications_model.dart';
import 'package:deeplom/data/models/main_model.dart';
import 'package:deeplom/data/models/notification_model.dart';
import 'package:equatable/equatable.dart';

class MainState extends Equatable {
  final Lce<MainModel> mainData;
  final Lce<List<ApplicationsModel>> applications;
  final Lce<List<NotificationModel>> notifications;
  final Uint8List? avatar;
  final bool allApplicationShow;

  List<DateTime> get currentMonthDates => List.generate(
        DateTime(DateTime.now().year, DateTime.now().month + 1, 0).day,
        (index) => DateTime(DateTime.now().year, DateTime.now().month, index + 1),
      );

  const MainState({
    this.mainData = const Lce.loading(),
    this.applications = const Lce.loading(),
    this.notifications = const Lce.loading(),
    this.avatar,
    this.allApplicationShow = false,
  });

  MainState copyWith({
    Lce<MainModel>? mainData,
    Lce<List<ApplicationsModel>>? applications,
    Lce<List<NotificationModel>>? notifications,
    Uint8List? avatar,
    bool? allApplicationShow,
  }) {
    return MainState(
      mainData: mainData ?? this.mainData,
      applications: applications ?? this.applications,
      notifications: notifications ?? this.notifications,
      avatar: avatar ?? this.avatar,
      allApplicationShow: allApplicationShow ?? this.allApplicationShow,
    );
  }

  @override
  List<Object?> get props => [mainData, applications, notifications, avatar, allApplicationShow];
}
