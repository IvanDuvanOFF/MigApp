class AuthModel {
  final String accessToken;
  final String refreshToken;
  final bool tfaEnabled;

  const AuthModel({
    required this.accessToken,
    required this.refreshToken,
    required this.tfaEnabled,
  });
}
