import 'package:deeplom/config/lce.dart';
import 'package:deeplom/data/models/auth_model.dart';
import 'package:deeplom/data/models/error_model.dart';
import 'package:flutter/foundation.dart';

abstract class AbstractAuthRepository {
  Future<AuthModel> signIn({required String login, required String password});

  ValueListenable<Lce<AuthModel>> get userData;

  ValueListenable<Lce<ErrorModel>> get errorData;

  Future<String> restorePass({String? phone, String? email});

  Future<void> restoreVerify({required String username, required String code});

  Future<void> userRestore({required String username, required String code, required String password, required String passwordConfirm});
}
