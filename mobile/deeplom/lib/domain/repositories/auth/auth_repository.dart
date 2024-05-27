
import 'package:deeplom/config/api_urls.dart';
import 'package:deeplom/config/lce.dart';
import 'package:deeplom/data/dto/auth_dto.dart';
import 'package:deeplom/data/dto/error_dto.dart';
import 'package:deeplom/data/mappers/auth_mapper.dart';
import 'package:deeplom/data/mappers/error_mapper.dart';
import 'package:deeplom/data/models/auth_model.dart';
import 'package:deeplom/data/models/error_model.dart';
import 'package:deeplom/domain/repositories/auth/abstract_auth_repository.dart';
import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';

class AuthRepository implements AbstractAuthRepository {
  AuthRepository({required this.dio});

  Dio dio;

  @override
  final ValueNotifier<Lce<AuthModel>> userData = ValueNotifier(const Lce.idle());

  @override
  final ValueNotifier<Lce<ErrorModel>> errorData = ValueNotifier(const Lce.idle());

  @override
  Future<AuthModel> signIn({required String login, required String password}) async {
    var data = {
      'login': login,
      'password': password,
    };
    try {
      var response = await dio.post(ApiUrls.signIn, data: data);
      var authData = response.data as Map<String, dynamic>;
      var authModel = AuthDto.fromJson(authData).toModel();
      return authModel;
    } on DioException catch (e) {
      if (e.response != null) {
        // Парсинг ошибки из тела ответа

        errorData.value = ErrorDto.fromJson(e.response!.data).toModel().asContent;
        throw Exception('Error: ${errorData.value.content?.message}');
      } else {
        // Обработка других типов ошибок
        throw Exception('Error: ${e.message}');
      }
    } catch (e) {
      throw Exception('Error: $e}');
    }
  }

  @override
  Future<void> restorePass({String? phone, String? email}) async {
    var emailData = {
      'email': email,
    };
    var phoneData = {
      'phone': phone,
    };
    await dio.post(ApiUrls.restorePass, data: phone == null ? emailData : phoneData);
  }
}
