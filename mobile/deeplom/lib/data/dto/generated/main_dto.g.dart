// GENERATED CODE - DO NOT MODIFY BY HAND

part of '../main_dto.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

MainDto _$MainDtoFromJson(Map<String, dynamic> json) => MainDto(
      userId: json['user_id'] as String?,
      logo: json['logo'] as String?,
      name: json['name'] as String?,
      surname: json['surname'] as String?,
      patronymic: json['patronymic'] as String?,
      photo: json['photo'] as String?,
      institute: json['institute'] as String?,
      group: json['group'] as String?,
    );

Map<String, dynamic> _$MainDtoToJson(MainDto instance) => <String, dynamic>{
      'user_id': instance.userId,
      'logo': instance.logo,
      'name': instance.name,
      'surname': instance.surname,
      'patronymic': instance.patronymic,
      'photo': instance.photo,
      'institute': instance.institute,
      'group': instance.group,
    };
