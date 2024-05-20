import 'package:deeplom/data/dto/profile_dto.dart';
import 'package:deeplom/data/models/profile_model.dart';

extension ProfileDtoExt on ProfileDto {
  ProfileModel toModel() {
    return ProfileModel(
      id: id ?? '',
      username: username ?? '',
      email: email ?? '',
      password: password ?? '',
      isActive: isActive ?? false,
      name: name ?? '',
      surname: surname ?? '',
      patronymic: patronymic ?? '',
      institute: institute ?? '',
      group: group ?? '',
      photo: photo ?? '',
      sex: sex ?? '',
      phoneNumber: phoneNumber ?? '',
      country: country ?? '',
      birthday: birthday != null ? DateTime.parse(birthday!) : DateTime.now(),
      status: status ?? '',
      role: role ?? '',
    );
  }
}
