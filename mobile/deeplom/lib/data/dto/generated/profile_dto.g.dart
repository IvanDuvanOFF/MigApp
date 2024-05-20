// GENERATED CODE - DO NOT MODIFY BY HAND

part of '../profile_dto.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ProfileDto _$ProfileDtoFromJson(Map<String, dynamic> json) => ProfileDto(
      id: json['id'] as String?,
      username: json['username'] as String?,
      email: json['email'] as String?,
      password: json['password'] as String?,
      isActive: json['is_active'] as bool?,
      name: json['name'] as String?,
      surname: json['surname'] as String?,
      patronymic: json['patronymic'] as String?,
      institute: json['institute'] as String?,
      group: json['group'] as String?,
      photo: json['photo'] as String?,
      sex: json['sex'] as String?,
      phoneNumber: json['phone_number'] as String?,
      country: json['country'] as String?,
      birthday: json['birthday'] as String?,
      status: json['status'] as String?,
      role: json['role'] as String?,
    );

Map<String, dynamic> _$ProfileDtoToJson(ProfileDto instance) => <String, dynamic>{
      'id': instance.id,
      'username': instance.username,
      'email': instance.email,
      'password': instance.password,
      'is_active': instance.isActive,
      'name': instance.name,
      'surname': instance.surname,
      'patronymic': instance.patronymic,
      'institute': instance.institute,
      'group': instance.group,
      'photo': instance.photo,
      'sex': instance.sex,
      'phone_number': instance.phoneNumber,
      'country': instance.country,
      'birthday': instance.birthday,
      'status': instance.status,
      'role': instance.role,
    };
