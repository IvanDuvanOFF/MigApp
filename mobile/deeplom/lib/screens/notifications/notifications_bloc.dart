import 'package:deeplom/config/lce.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/screens/notifications/notifications_events.dart';
import 'package:deeplom/screens/notifications/notifications_state.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class NotificationsBloc extends Bloc<NotificationsEvents, NotificationsState> {
  NotificationsBloc(super.initialState, {required this.mainRepository}) {
    on<OnInit>(_onInit);
    on<DeleteNotification>(_onDeleteNotification);
  }

  final AbstractMainRepository mainRepository;
  final _secureStorage = const FlutterSecureStorage();

  Future<void> _onInit(OnInit event, Emitter<NotificationsState> emit) async {
    var notifications = await mainRepository.getNotifications();
    // List<NotificationModel> notifications = [
    //   NotificationModel(
    //     notificationId: 1,
    //     userId: 'user1',
    //     title: 'Notification One',
    //     description: 'This is the first notification',
    //     date: DateTime.now(),
    //     isViewed: true,
    //     status: 'new',
    //   ),
    //   NotificationModel(
    //     notificationId: 2,
    //     userId: 'user2',
    //     title: 'Notification Two',
    //     description: 'This is the second notification',
    //     date: DateTime.now(),
    //     isViewed: true,
    //     status: 'read',
    //   ),
    //   NotificationModel(
    //     notificationId: 3,
    //     userId: 'user3',
    //     title: 'Notification Three',
    //     description: 'This is the third notification',
    //     date: DateTime.now(),
    //     isViewed: true,
    //     status: 'archived',
    //   ),
    //   NotificationModel(
    //     notificationId: 4,
    //     userId: 'user4',
    //     title: 'Notification Four',
    //     description: 'This is the fourth notification',
    //     date: DateTime.now(),
    //     isViewed: true,
    //     status: 'new',
    //   ),
    //   NotificationModel(
    //     notificationId: 5,
    //     userId: 'user5',
    //     title: 'Notification Five',
    //     description: 'This is the fifth notification',
    //     date: DateTime.now(),
    //     isViewed: false,
    //     status: 'read',
    //   ),
    // ];
    emit(state.copyWith(notifications: notifications.asContent));
  }

  Future<void> _onDeleteNotification(DeleteNotification event, Emitter<NotificationsState> emit) async {
    await mainRepository.deleteNotification(notificationId: event.notificationId);
  }
}
