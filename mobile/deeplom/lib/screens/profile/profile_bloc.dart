import 'package:deeplom/config/lce.dart';
import 'package:deeplom/config/navigation.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/screens/profile/profile_events.dart';
import 'package:deeplom/screens/profile/profile_state.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class ProfileBloc extends Bloc<ProfileEvents, ProfileState> {
  ProfileBloc(super.initialState, {required this.mainRepository}) {
    on<OnInit>(_onInit);
    on<OnOldObscurePass>(_onOldObscurePass);
    on<OnNewObscurePass>(_onNewObscurePass);
    on<Logout>(_onLogout);
    on<ChangePassword>(_onChangePassword);
  }

  final AbstractMainRepository mainRepository;
  final _secureStorage = const FlutterSecureStorage();

  Future<void> _onInit(OnInit event, Emitter<ProfileState> emit) async {
    var profileData = await mainRepository.getProfileData();
    emit(state.copyWith(profile: profileData.asContent));
    // await mainRepository.getMainData();
  }

  Future<void> _onOldObscurePass(OnOldObscurePass event, Emitter<ProfileState> emit) async {
    emit(state.copyWith(oldPassIsObscure: !state.oldPassIsObscure));
  }

  Future<void> _onNewObscurePass(OnNewObscurePass event, Emitter<ProfileState> emit) async {
    emit(state.copyWith(newPassIsObscure: !state.newPassIsObscure));
  }

  Future<void> _onChangePassword(ChangePassword event, Emitter<ProfileState> emit) async {
    await mainRepository.changePassword(oldPassword: event.oldPassword, newPassword: event.newPassword);
  }

  Future<void> _onLogout(Logout event, Emitter<ProfileState> emit) async {
    await mainRepository.logOut();
    await _secureStorage.delete(key: 'token');
    await _secureStorage.delete(key: 'refreshToken');
    await _secureStorage.delete(key: 'userID');
    AppRouting.toAuth();
  }
}
