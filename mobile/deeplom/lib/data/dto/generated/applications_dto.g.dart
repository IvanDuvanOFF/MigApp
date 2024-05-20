// GENERATED CODE - DO NOT MODIFY BY HAND

part of '../applications_dto.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ApplicationsDto _$ApplicationsDtoFromJson(Map<String, dynamic> json) => ApplicationsDto(
      id: json['id'] as String?,
      title: json['title'] as String?,
      status: json['status'] as String?,
    );

Map<String, dynamic> _$ApplicationsDtoToJson(ApplicationsDto instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'status': instance.status,
    };

SelectedApplicationDto _$SelectedApplicationDtoFromJson(Map<String, dynamic> json) => SelectedApplicationDto(
      id: json['id'] as String?,
      title: json['title'] as String?,
      status: json['status'] as String?,
      documents: (json['documents'] as List<dynamic>?)?.map((e) => DocumentDto.fromJson(e as Map<String, dynamic>)).toList(),
    );

Map<String, dynamic> _$SelectedApplicationDtoToJson(SelectedApplicationDto instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'status': instance.status,
      'documents': instance.documents,
    };

DocumentDto _$DocumentDtoFromJson(Map<String, dynamic> json) => DocumentDto(
      id: json['id'] as String?,
      title: json['title'] as String?,
      link: json['link'] as String?,
      status: json['status'] as String?,
    );

Map<String, dynamic> _$DocumentDtoToJson(DocumentDto instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'link': instance.link,
      'status': instance.status,
    };
