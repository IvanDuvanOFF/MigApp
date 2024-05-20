import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'generated/main_dto.g.dart';

@JsonSerializable()
class MainDto extends Equatable {
  @JsonKey(name: 'user_id')
  final String? userId;
  final String? logo;
  final String? name;
  final String? surname;
  final String? patronymic;
  final String? photo;
  final String? institute;
  final String? group;

  const MainDto({
    this.userId,
    this.logo,
    this.name,
    this.surname,
    this.patronymic,
    this.photo,
    this.institute,
    this.group,
  });

  factory MainDto.fromJson(Map<String, dynamic> json) => _$MainDtoFromJson(json);
  Map<String, dynamic> toJson() => _$MainDtoToJson(this);

  @override
  List<Object?> get props => [
        userId,
        logo,
        name,
        surname,
        patronymic,
        photo,
        institute,
        group,
      ];
}
