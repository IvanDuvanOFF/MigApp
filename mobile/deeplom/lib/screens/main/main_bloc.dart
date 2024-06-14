import 'package:deeplom/config/lce.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/screens/main/main_events.dart';
import 'package:deeplom/screens/main/main_state.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class MainBloc extends Bloc<MainEvents, MainState> {
  MainBloc(super.initialState, {required this.mainRepository}) {
    on<OnInit>(_onInit);
    on<GetApplicationByDate>(_getApplicationByDate);
    on<GetAllApplication>(_getAllApplication);
  }

  final AbstractMainRepository mainRepository;
  final _secureStorage = const FlutterSecureStorage();

  Future<void> _onInit(OnInit event, Emitter<MainState> emit) async {
    var mainData = await mainRepository.getMainData();
    var notifications = await mainRepository.getNotifications();
    await mainRepository.putFirebaseDate();
    var applicationData = await mainRepository.getApplications(date: DateTime.now().toString());
    if (mainData.photo.isNotEmpty) {
      var getFile = await mainRepository.getFile(fileName: mainData.photo);
      emit(state.copyWith(avatar: getFile));
    }
    emit(
      state.copyWith(
        mainData: mainData.asContent,
        applications: applicationData.asContent,
        notifications: notifications.asContent,
      ),
    );
  }

  Future<void> _getApplicationByDate(GetApplicationByDate event, Emitter<MainState> emit) async {
    try {
      var applicationData = await mainRepository.getApplications(date: event.date);
      emit(state.copyWith(applications: applicationData.asContent, allApplicationShow: false));
    } catch (e) {
      emit(
        state.copyWith(
          applications: const Lce.idle(),
        ),
      );
    }
  }

  Future<void> _getAllApplication(GetAllApplication event, Emitter<MainState> emit) async {
    try {
      var applicationData = await mainRepository.getApplications();
      emit(state.copyWith(applications: applicationData.asContent, allApplicationShow: true));
    } catch (e) {
      emit(
        state.copyWith(
          applications: const Lce.idle(),
        ),
      );
    }
  }
}
