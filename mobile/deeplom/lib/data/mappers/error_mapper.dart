import 'package:deeplom/data/dto/error_dto.dart';
import 'package:deeplom/data/models/error_model.dart';

extension ErrorDtoExt on ErrorDto {
  ErrorModel toModel() {
    return ErrorModel(
      status: const StatusDto().toModel(),
      message: message ?? '',
    );
  }
}

extension StatusDtoExt on StatusDto {
  StatusModel toModel() {
    return StatusModel(
      code: code ?? 0,
      name: name ?? '',
    );
  }
}
