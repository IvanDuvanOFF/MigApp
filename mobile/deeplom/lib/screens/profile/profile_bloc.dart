import 'dart:io';

import 'package:deeplom/config/lce.dart';
import 'package:deeplom/config/navigation.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/screens/add_application/add_application_screen.dart';
import 'package:deeplom/screens/profile/profile_events.dart';
import 'package:deeplom/screens/profile/profile_state.dart';
import 'package:dio/dio.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:image_picker/image_picker.dart';

class ProfileBloc extends Bloc<ProfileEvents, ProfileState> {
  ProfileBloc(super.initialState, {required this.mainRepository}) {
    on<OnInit>(_onInit);
    on<OnOldObscurePass>(_onOldObscurePass);
    on<OnNewObscurePass>(_onNewObscurePass);
    on<Logout>(_onLogout);
    on<ChangePassword>(_onChangePassword);
    on<ChoiceAvatar>(_choiceAvatar);
  }

  final AbstractMainRepository mainRepository;
  final _secureStorage = const FlutterSecureStorage();

  Future<void> _onInit(OnInit event, Emitter<ProfileState> emit) async {
    var profileData = await mainRepository.getProfileData();
    if (profileData.photo.isNotEmpty) {
      var getFile = await mainRepository.getFile(fileName: profileData.photo);
      emit(state.copyWith(avatar: getFile));
    }
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
    if (event.oldPassword.isNotEmpty && event.newPassword.isNotEmpty) {
      try {
        await mainRepository.changePassword(oldPassword: event.oldPassword, newPassword: event.newPassword);
        emit(state.copyWith(passChangeSuccess: true));
        Future.delayed(const Duration(seconds: 5));
        emit(state.copyWith(passChangeSuccess: false));
      } on DioException catch (e) {
        emit(state.copyWith(error: 'Error: ${e.response?.statusCode}'));
      }
    } else if (event.oldPassword.isEmpty && event.newPassword.isEmpty) {
      emit(state.copyWith(error: 'Необходимо заполнить поля'));
    } else if (event.newPassword.isEmpty) {
      emit(state.copyWith(error: 'Заполните новый пароль'));
    } else {
      emit(state.copyWith(error: 'Заполните старый пароль'));
    }
  }

  Future<void> _choiceAvatar(ChoiceAvatar event, Emitter<ProfileState> emit) async {
    final ImagePicker picker = ImagePicker();
    XFile? xFile;
    File? file;

    if (event.sourceType == FileSourceType.gallery) {
      xFile = await picker.pickImage(source: ImageSource.gallery);
    } else if (event.sourceType == FileSourceType.camera) {
      xFile = await picker.pickImage(source: ImageSource.camera);
    } else if (event.sourceType == FileSourceType.device) {
      FilePickerResult? result = await FilePicker.platform.pickFiles();
      if (result != null && result.files.single.path != null) {
        file = File(result.files.single.path!);
      }
    }

    if (xFile != null) {
      file = File(xFile.path);
    }

    if (file != null) {
      final fileData = await mainRepository.uploadFile(file: file);
      await mainRepository.uploadAvatar(name: fileData?.name ?? '', link: fileData?.link ?? '');
      AppRouting.toMenu();
    }
  }

  Future<void> _onLogout(Logout event, Emitter<ProfileState> emit) async {
    // await mainRepository.logOut();
    await _secureStorage.delete(key: 'token');
    await _secureStorage.delete(key: 'refreshToken');
    await _secureStorage.delete(key: 'userID');
    AppRouting.toAuth();
  }
}
