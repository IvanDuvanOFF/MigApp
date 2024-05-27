// GENERATED CODE - DO NOT MODIFY BY HAND

part of '../error_dto.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ErrorDto _$ErrorDtoFromJson(Map<String, dynamic> json) => ErrorDto(
      status: json['status'] == null ? null : StatusDto.fromJson(json['status'] as Map<String, dynamic>),
      message: json['message'] as String?,
    );

Map<String, dynamic> _$ErrorDtoToJson(ErrorDto instance) => <String, dynamic>{
      'status': instance.status,
      'message': instance.message,
    };

StatusDto _$StatusDtoFromJson(Map<String, dynamic> json) => StatusDto(
      code: (json['code'] as num?)?.toInt(),
      name: json['name'] as String?,
    );

Map<String, dynamic> _$StatusDtoToJson(StatusDto instance) => <String, dynamic>{
      'code': instance.code,
      'name': instance.name,
    };
