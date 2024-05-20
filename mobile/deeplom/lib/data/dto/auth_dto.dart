import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'generated/auth_dto.g.dart';

@JsonSerializable()
class AuthDto extends Equatable {
  @JsonKey(name: 'access_token')
  final String? accessToken;
  @JsonKey(name: 'refresh_token')
  final String? refreshToken;
  @JsonKey(name: 'tfa_enabled')
  final bool? tfaEnabled;

  const AuthDto({
    this.accessToken,
    this.refreshToken,
    this.tfaEnabled,
  });

  factory AuthDto.fromJson(Map<String, dynamic> json) => _$AuthDtoFromJson(json);
  Map<String, dynamic> toJson() => _$AuthDtoToJson(this);

  @override
  List<Object?> get props => [
        accessToken,
        refreshToken,
        tfaEnabled,
      ];
}
