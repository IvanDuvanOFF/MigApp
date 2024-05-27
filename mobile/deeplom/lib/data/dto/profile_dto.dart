import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'generated/profile_dto.g.dart';

@JsonSerializable()
class ProfileDto extends Equatable {
  final String? id;
  final String? username;
  final String? email;
  final String? password;
  @JsonKey(name: 'is_active')
  final bool? isActive;
  final String? name;
  final String? surname;
  final String? patronymic;
  final String? institute;
  final String? group;
  final String? photo;
  final String? sex;
  @JsonKey(name: 'phone_number')
  final String? phoneNumber;
  final String? country;
  final String? birthday;
  final String? status;
  final String? role;

  const ProfileDto({
    this.id,
    this.username,
    this.email,
    this.password,
    this.isActive,
    this.name,
    this.surname,
    this.patronymic,
    this.institute,
    this.group,
    this.photo,
    this.sex,
    this.phoneNumber,
    this.country,
    this.birthday,
    this.status,
    this.role,
  });

  factory ProfileDto.fromJson(Map<String, dynamic> json) => _$ProfileDtoFromJson(json);
  Map<String, dynamic> toJson() => _$ProfileDtoToJson(this);

  @override
  List<Object?> get props => [
        id,
        username,
        email,
        password,
        isActive,
        name,
        surname,
        patronymic,
        institute,
        group,
        photo,
        sex,
        phoneNumber,
        country,
        birthday,
        status,
        role,
      ];
}
