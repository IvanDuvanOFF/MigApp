import 'package:deeplom/data/dto/main_dto.dart';
import 'package:deeplom/data/models/main_model.dart';

extension MainDtoExt on MainDto {
  MainModel toModel() {
    return MainModel(
      userId: userId ?? '',
      logo: logo ?? '',
      name: name ?? '',
      surname: surname ?? '',
      patronymic: patronymic ?? '',
      photo: photo ?? '',
      institute: institute ?? '',
      group: group ?? '',
    );
  }
}
