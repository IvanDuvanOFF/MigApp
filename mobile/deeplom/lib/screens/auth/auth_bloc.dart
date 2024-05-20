import 'package:deeplom/config/navigation.dart';
import 'package:deeplom/domain/repositories/auth/abstract_auth_repository.dart';
import 'package:deeplom/screens/auth/auth_events.dart';
import 'package:deeplom/screens/auth/auth_state.dart';
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
  }

  final AbstractAuthRepository authRepository;
  final _secureStorage = const FlutterSecureStorage();

  Future<void> _onInit(OnInit event, Emitter<AuthState> emit) async {
    LanguageCode currentLang = LanguageCodeExtension.fromCode(await _secureStorage.read(key: 'langCode') ?? '');
    emit(state.copyWith(selectedLang: currentLang));
  }

  Future<void> _onAuth(OnAuth event, Emitter<AuthState> emit) async {
    try {
      var auth = await authRepository.signIn(login: event.login, password: event.password);
      print('TEST TOKEN: $auth');
      await _secureStorage.write(key: 'token', value: auth.accessToken);
      await _secureStorage.write(key: 'refreshToken', value: auth.refreshToken);
      AppRouting.toMenu();
    } catch (e) {
      emit(state.copyWith(error: authRepository.errorData.value.content?.message));
    }
  }

  Future<void> _onResetPass(OnResetPassword event, Emitter<AuthState> emit) async {
    state.resetType == ResetPasswordType.sms
        ? await authRepository.restorePass(phone: event.resetType)
        : await authRepository.restorePass(email: event.resetType);
  }

  Future<void> _changeLanguage(ChangeLanguage event, Emitter<AuthState> emit) async {
    await _secureStorage.write(key: 'langCode', value: event.selectedLanguage.code);
    emit(state.copyWith(selectedLang: event.selectedLanguage));
  }

  Future<void> _onObscurePass(OnObscurePass event, Emitter<AuthState> emit) async {
    emit(state.copyWith(passIsObscure: !state.passIsObscure));
  }

  Future<void> _choiceResetType(ChoiceResetType event, Emitter<AuthState> emit) async {
    emit(state.copyWith(resetType: event.type));
  }
}
