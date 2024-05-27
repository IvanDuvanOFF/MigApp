import 'package:deeplom/config/lce.dart';
import 'package:deeplom/data/models/auth_model.dart';
import 'package:deeplom/data/models/error_model.dart';
import 'package:flutter/foundation.dart';

abstract class AbstractAuthRepository {
  Future<AuthModel> signIn({required String login, required String password});

  ValueListenable<Lce<AuthModel>> get userData;

  ValueListenable<Lce<ErrorModel>> get errorData;

  Future<void> restorePass({String? phone, String? email});
}
