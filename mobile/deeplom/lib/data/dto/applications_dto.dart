import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'applications_dto.g.dart';

@JsonSerializable()
class ApplicationsDto extends Equatable {
  final String? id;
  final String? title;
  final String? status;
  final String? date;

  const ApplicationsDto({
    this.id,
    this.title,
    this.status,
    this.date,
  });

  factory ApplicationsDto.fromJson(Map<String, dynamic> json) => _$ApplicationsDtoFromJson(json);
  Map<String, dynamic> toJson() => _$ApplicationsDtoToJson(this);

  @override
  List<Object?> get props => [id, title, status, date];
}

@JsonSerializable()
class SelectedApplicationDto extends Equatable {
  final String? id;
  final String? title;
  final String? status;
  final String? date;
  final List<DocumentDto>? documents;

  const SelectedApplicationDto({
    this.id,
    this.title,
    this.status,
    this.date,
    this.documents,
  });

  factory SelectedApplicationDto.fromJson(Map<String, dynamic> json) => _$SelectedApplicationDtoFromJson(json);
  Map<String, dynamic> toJson() => _$SelectedApplicationDtoToJson(this);

  @override
  List<Object?> get props => [id, title, status, date, documents];
}

@JsonSerializable()
class DocumentDto extends Equatable {
  final String? id;
  final String? title;
  final String? status;
  final String? creationDate;
  final String? expirationDate;
  final String? fileName;

  const DocumentDto({
    this.id,
    this.title,
    this.status,
    this.creationDate,
    this.expirationDate,
    this.fileName,
  });

  factory DocumentDto.fromJson(Map<String, dynamic> json) => _$DocumentDtoFromJson(json);
  Map<String, dynamic> toJson() => _$DocumentDtoToJson(this);

  @override
  List<Object?> get props => [id, title, status, creationDate, expirationDate, fileName];
}
