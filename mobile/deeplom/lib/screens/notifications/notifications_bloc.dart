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

  Future<void> _onInit(OnInit event, Emitter<NotificationsState> emit) async {
    var notifications = await mainRepository.getNotifications();
    emit(state.copyWith(notifications: notifications.asContent));
  }

  Future<void> _onDeleteNotification(DeleteNotification event, Emitter<NotificationsState> emit) async {
    await mainRepository.deleteNotification(notificationId: event.notificationId);
    add(OnInit());
  }
}
