import 'package:deeplom/screens/auth/auth_state.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';

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
  OnResetPassword({required this.resetType, this.email = '', this.phone = ''});
  final String resetType;
  final String email;
  final String phone;

  @override
  List<Object> get props => [resetType, email, phone];
}

class OnUserPasswordRestore extends AuthEvents {
  OnUserPasswordRestore({required this.password, required this.passwordConfirm});
  final String password;
  final String passwordConfirm;
  @override
  List<Object> get props => [password, passwordConfirm];
}

class OnRestoreVerify extends AuthEvents {
  OnRestoreVerify({required this.code});
  final String code;
  @override
  List<Object> get props => [code];
}

class ChangeLanguage extends AuthEvents {
  ChangeLanguage({required this.locale});
  final Locale locale;

  @override
  List<Object> get props => [locale];
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
