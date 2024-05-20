// GENERATED CODE - DO NOT MODIFY BY HAND

part of '../auth_dto.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

AuthDto _$AuthDtoFromJson(Map<String, dynamic> json) => AuthDto(
      accessToken: json['access_token'] as String?,
      refreshToken: json['refresh_token'] as String?,
      tfaEnabled: json['tfa_enabled'] as bool?,
    );

Map<String, dynamic> _$AuthDtoToJson(AuthDto instance) => <String, dynamic>{
      'access_token': instance.accessToken,
      'refresh_token': instance.refreshToken,
      'tfa_enabled': instance.tfaEnabled,
    };
