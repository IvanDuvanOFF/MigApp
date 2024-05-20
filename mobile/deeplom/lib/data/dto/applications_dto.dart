import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'generated/applications_dto.g.dart';

@JsonSerializable()
class ApplicationsDto extends Equatable {
  final String? id;
  final String? title;
  final String? status;

  const ApplicationsDto({
    this.id,
    this.title,
    this.status,
  });

  factory ApplicationsDto.fromJson(Map<String, dynamic> json) => _$ApplicationsDtoFromJson(json);
  Map<String, dynamic> toJson() => _$ApplicationsDtoToJson(this);

  @override
  List<Object?> get props => [id, title, status];
}

@JsonSerializable()
class SelectedApplicationDto extends Equatable {
  final String? id;
  final String? title;
  final String? status;
  final List<DocumentDto>? documents;

  const SelectedApplicationDto({
    this.id,
    this.title,
    this.status,
    this.documents,
  });

  factory SelectedApplicationDto.fromJson(Map<String, dynamic> json) => _$SelectedApplicationDtoFromJson(json);
  Map<String, dynamic> toJson() => _$SelectedApplicationDtoToJson(this);

  @override
  List<Object?> get props => [id, title, status, documents];
}

@JsonSerializable()
class DocumentDto extends Equatable {
  final String? id;
  final String? title;
  final String? link;
  final String? status;

  const DocumentDto({
    this.id,
    this.title,
    this.link,
    this.status,
  });

  factory DocumentDto.fromJson(Map<String, dynamic> json) => _$DocumentDtoFromJson(json);
  Map<String, dynamic> toJson() => _$DocumentDtoToJson(this);

  @override
  List<Object?> get props => [id, title, link, status];
}
