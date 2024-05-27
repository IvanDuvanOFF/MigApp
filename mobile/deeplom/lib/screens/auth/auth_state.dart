import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';

enum LanguageCode { ru, en }

enum ResetPasswordType { email, sms, choice }

class AuthState extends Equatable {
  final String login;
  final String password;
  final bool passIsObscure;
  final LanguageCode selectedLang;
  final ResetPasswordType resetType;
  final String error;

  const AuthState({
    this.login = '',
    this.password = '',
    this.passIsObscure = true,
    this.selectedLang = LanguageCode.ru,
    this.resetType = ResetPasswordType.choice,
    this.error = '',
  });

  AuthState copyWith({
    String? login,
    String? password,
    bool? passIsObscure,
    LanguageCode? selectedLang,
    String? error,
    ResetPasswordType? resetType,
  }) {
    return AuthState(
      login: login ?? this.login,
      password: password ?? this.password,
      passIsObscure: passIsObscure ?? this.passIsObscure,
      selectedLang: selectedLang ?? this.selectedLang,
      error: error ?? this.error,
      resetType: resetType ?? this.resetType,
    );
  }

  @override
  List<Object?> get props => [login, password, passIsObscure, selectedLang, error, resetType];
}

extension LanguageCodeExtension on LanguageCode {
  String get name {
    switch (this) {
      case LanguageCode.ru:
        return 'Русский';
      case LanguageCode.en:
        return 'English';
      default:
        return '';
    }
  }

  String get code {
    switch (this) {
      case LanguageCode.ru:
        return 'RU';
      case LanguageCode.en:
        return 'EN';
      default:
        return '';
    }
  }

  Widget get flag {
    switch (this) {
      case LanguageCode.ru:
        return const Icon(Icons.flag, color: Colors.red);
      case LanguageCode.en:
        return const Icon(Icons.flag, color: Colors.blue);
      default:
        return Container();
    }
  }

  static LanguageCode fromCode(String code) {
    switch (code) {
      case 'RU':
        return LanguageCode.ru;
      case 'EN':
        return LanguageCode.en;
      default:
        return LanguageCode.ru;
    }
  }
}
