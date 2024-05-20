import 'package:deeplom/screens/auth/auth_state.dart';
import 'package:equatable/equatable.dart';

abstract class AuthEvents extends Equatable {}

class OnAuth extends AuthEvents {
  OnAuth({required this.login, required this.password});
  final String login;
  final String password;

  @override
  List<Object> get props => [login, password];
}

class OnInit extends AuthEvents {
  OnInit();

  @override
  List<Object> get props => [];
}

class OnResetPassword extends AuthEvents {
  OnResetPassword({required this.resetType});
  final String resetType;

  @override
  List<Object> get props => [resetType];
}

class ChangeLanguage extends AuthEvents {
  ChangeLanguage({required this.selectedLanguage});
  final LanguageCode selectedLanguage;

  @override
  List<Object> get props => [selectedLanguage];
}

class OnObscurePass extends AuthEvents {
  OnObscurePass();

  @override
  List<Object> get props => [];
}

class ChoiceResetType extends AuthEvents {
  ChoiceResetType({required this.type});
  final ResetPasswordType type;

  @override
  List<Object> get props => [type];
}
