import 'dart:ui';

import 'package:deeplom/config/navigation.dart';
import 'package:deeplom/domain/repositories/auth/abstract_auth_repository.dart';
import 'package:deeplom/screens/auth/auth_events.dart';
import 'package:deeplom/screens/auth/auth_state.dart';
import 'package:dio/dio.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class AuthBloc extends Bloc<AuthEvents, AuthState> {
  AuthBloc(super.initialState, {required this.authRepository}) {
    on<OnInit>(_onInit);
    on<OnAuth>(_onAuth);
    on<OnResetPassword>(_onResetPass);
    on<OnObscurePass>(_onObscurePass);
    on<ChangeLanguage>(_changeLanguage);
    on<ChoiceResetType>(_choiceResetType);
    on<OnRestoreVerify>(_onRestoreVerify);
    on<OnUserPasswordRestore>(_onUserPasswordRestore);
  }

  final AbstractAuthRepository authRepository;
  final _secureStorage = const FlutterSecureStorage();

  Future<void> _onInit(OnInit event, Emitter<AuthState> emit) async {
    LanguageCode currentLang = LanguageCodeExtension.fromCode(await _secureStorage.read(key: 'langCode') ?? '');
    emit(state.copyWith(selectedLang: Locale(currentLang.code)));
  }

  Future<void> _onAuth(OnAuth event, Emitter<AuthState> emit) async {
    try {
      var auth = await authRepository.signIn(login: event.login, password: event.password);
      print('TEST TOKEN: ${auth.accessToken}');
      await _secureStorage.write(key: 'token', value: auth.accessToken);
      await _secureStorage.write(key: 'refreshToken', value: auth.refreshToken);
      AppRouting.toMenu();
    } catch (e) {
      emit(state.copyWith(error: authRepository.errorData.value.content?.message));
    }
  }

  Future<void> _onResetPass(OnResetPassword event, Emitter<AuthState> emit) async {
    String username;
    state.resetType == ResetPasswordType.sms
        ? username = await authRepository.restorePass(phone: event.resetType)
        : username = await authRepository.restorePass(email: event.resetType);

    if (username.isNotEmpty) {
      emit(state.copyWith(
        isRestoreSending: true,
        phone: state.resetType == ResetPasswordType.sms ? event.resetType : '',
        email: state.resetType == ResetPasswordType.email ? event.resetType : '',
        login: username,
      ));
    }
  }

  Future<void> _onUserPasswordRestore(OnUserPasswordRestore event, Emitter<AuthState> emit) async {
    try {
      await authRepository.userRestore(
        username: state.login,
        code: state.otpCode,
        password: event.password,
        passwordConfirm: event.passwordConfirm,
      );
      emit(state.copyWith(restoreReady: true));
      // Future.delayed(const Duration(seconds: 2));
      // AppRouting.toAuth();
    } on DioException catch (e) {
      print('RESTORE VERIFY ERROR:: $e');
    }
  }

  Future<void> _onRestoreVerify(OnRestoreVerify event, Emitter<AuthState> emit) async {
    if (event.code.isEmpty) {
      emit(state.copyWith(error: 'Заполните поле'));
    } else {
      try {
        await authRepository.restoreVerify(username: state.login, code: event.code);
        emit(state.copyWith(isRestoreSending: false, isRestoreVerify: true, otpCode: event.code));
      } on DioException catch (e) {
        emit(state.copyWith(error: 'Неверный OTP код'));
        print('RESTORE VERIFY ERROR:: $e');
      }
    }
  }

  Future<void> _changeLanguage(ChangeLanguage event, Emitter<AuthState> emit) async {
    await _secureStorage.write(key: 'langCode', value: event.locale.languageCode);
    print('SELECTED LOCALE :: ${event.locale}');
    emit(state.copyWith(selectedLang: event.locale));
  }

  Stream<AuthState> mapEventToState(AuthEvents event) async* {
    if (event is ChangeLanguage) {
      print('STREAM LOADED');
      add(ChangeLanguage(locale: event.locale));
    }
  }

  Future<void> _onObscurePass(OnObscurePass event, Emitter<AuthState> emit) async {
    emit(state.copyWith(passIsObscure: !state.passIsObscure));
  }

  Future<void> _choiceResetType(ChoiceResetType event, Emitter<AuthState> emit) async {
    emit(state.copyWith(resetType: event.type));
  }
}
