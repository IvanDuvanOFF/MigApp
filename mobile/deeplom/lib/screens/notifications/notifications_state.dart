import 'package:deeplom/config/lce.dart';
import 'package:deeplom/data/models/notification_model.dart';
import 'package:equatable/equatable.dart';

class NotificationsState extends Equatable {
  final Lce<List<NotificationModel>> notifications;
  const NotificationsState({
    this.notifications = const Lce.loading(),
  });

  NotificationsState copyWith({
    Lce<List<NotificationModel>>? notifications,
  }) {
    return NotificationsState(
      notifications: notifications ?? this.notifications,
    );
  }

  @override
  List<Object?> get props => [notifications];
}
