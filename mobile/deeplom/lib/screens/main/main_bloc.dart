import 'package:deeplom/config/lce.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/screens/main/main_events.dart';
import 'package:deeplom/screens/main/main_state.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class MainBloc extends Bloc<MainEvents, MainState> {
  MainBloc(super.initialState, {required this.mainRepository}) {
    on<OnInit>(_onInit);
  }

  final AbstractMainRepository mainRepository;
  final _secureStorage = const FlutterSecureStorage();

  Future<void> _onInit(OnInit event, Emitter<MainState> emit) async {
    var mainData = await mainRepository.getMainData();
    var notifications = await mainRepository.getNotifications();
    print('NOTIFICATIONS:: $notifications');
    await mainRepository.putFirebaseDate();

    try {
      var applicationData = await mainRepository.getApplications();
    } catch (e) {
      emit(
        state.copyWith(
          mainData: mainData.asContent,
          applications: const Lce.idle(),
          notifications: notifications.asContent,
        ),
      );
    }
    //Список для теста
    // List<ApplicationsModel> applicationData = [
    //   const ApplicationsModel(id: '1', title: 'Application One', status: ApplicationStatus.in_progress),
    //   const ApplicationsModel(id: '2', title: 'Application Two', status: ApplicationStatus.in_progress),
    //   const ApplicationsModel(id: '3', title: 'Application Three', status: ApplicationStatus.in_progress),
    //   const ApplicationsModel(id: '4', title: 'Application Four', status: ApplicationStatus.in_progress),
    //   const ApplicationsModel(id: '5', title: 'Application Five', status: ApplicationStatus.in_progress),
    // ];

    // List<NotificationModel> notificationsTest = [
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

    emit(
      state.copyWith(
        mainData: mainData.asContent,
        // applications: applicationData.asContent,
        notifications: notifications.asContent,
      ),
    );
  }
}
