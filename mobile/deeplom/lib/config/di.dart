import 'package:deeplom/domain/repositories/auth/abstract_auth_repository.dart';
import 'package:deeplom/domain/repositories/auth/auth_repository.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/domain/repositories/main/main_repository.dart';
import 'package:dio/dio.dart';
import 'package:get_it/get_it.dart';

void diRegister() async {
  Dio dio = Dio();

  GetIt.I.registerLazySingleton<AbstractAuthRepository>(
    () => AuthRepository(
      dio: dio,
    ),
  );

  GetIt.I.registerLazySingleton<AbstractMainRepository>(
    () => MainRepository(
      dio: dio,
    ),
  );
}
