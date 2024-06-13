import 'package:deeplom/data/dto/upload_file_dto.dart';
import 'package:deeplom/data/models/upload_file_model.dart';

extension UploadFileDtoExt on UploadFileDto {
  UploadFileModel toModel() {
    return UploadFileModel(
      name: name ?? '',
      link: link ?? '',
    );
  }
}
