import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';

enum LanguageCode { ru, en }

enum ResetPasswordType { email, sms, choice }

class AuthState extends Equatable {
  final String login;
  final String password;
  final bool passIsObscure;
  final Locale selectedLang;
  final ResetPasswordType resetType;
  final String error;
  final bool isRestoreSending;
  final bool isRestoreVerify;
  final String otpCode;
  final String email;
  final String phone;
  final bool restoreReady;

  const AuthState({
    this.login = '',
    this.password = '',
    this.passIsObscure = true,
    this.selectedLang = const Locale('ru'),
    this.resetType = ResetPasswordType.choice,
    this.error = '',
    this.isRestoreSending = false,
    this.isRestoreVerify = false,
    this.otpCode = '',
    this.email = '',
    this.phone = '',
    this.restoreReady = false,
  });

  AuthState copyWith({
    String? login,
    String? password,
    bool? passIsObscure,
    Locale? selectedLang,
    String? error,
    ResetPasswordType? resetType,
    bool? isRestoreSending,
    bool? isRestoreVerify,
    String? otpCode,
    String? email,
    String? phone,
    bool? restoreReady,
  }) {
    return AuthState(
      login: login ?? this.login,
      password: password ?? this.password,
      passIsObscure: passIsObscure ?? this.passIsObscure,
      selectedLang: selectedLang ?? this.selectedLang,
      error: error ?? this.error,
      resetType: resetType ?? this.resetType,
      isRestoreSending: isRestoreSending ?? this.isRestoreSending,
      isRestoreVerify: isRestoreVerify ?? this.isRestoreVerify,
      otpCode: otpCode ?? this.otpCode,
      email: email ?? this.email,
      phone: phone ?? this.phone,
      restoreReady: restoreReady ?? this.restoreReady,
    );
  }

  @override
  List<Object?> get props => [
        login,
        password,
        passIsObscure,
        selectedLang,
        error,
        resetType,
        isRestoreSending,
        isRestoreVerify,
        otpCode,
        email,
        phone,
        restoreReady,
      ];
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
        return 'ru';
      case LanguageCode.en:
        return 'en';
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
