import 'package:equatable/equatable.dart';

abstract class NotificationsEvents extends Equatable {}

class OnInit extends NotificationsEvents {
  OnInit();

  @override
  List<Object> get props => [];
}

class DeleteNotification extends NotificationsEvents {
  DeleteNotification({required this.notificationId});

  final int notificationId;
  @override
  List<Object> get props => [notificationId];
}