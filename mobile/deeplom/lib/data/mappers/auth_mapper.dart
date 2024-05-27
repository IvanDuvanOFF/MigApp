import 'package:deeplom/data/dto/auth_dto.dart';
import 'package:deeplom/data/models/auth_model.dart';

extension AuthDtoExt on AuthDto {
  AuthModel toModel() {
    return AuthModel(
      accessToken: accessToken ?? '',
      refreshToken: refreshToken ?? '',
      tfaEnabled: tfaEnabled ?? false,
    );
  }
}
