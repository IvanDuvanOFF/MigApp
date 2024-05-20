class ProfileModel {
  final String id;
  final String username;
  final String email;
  final String password;
  final bool isActive;
  final String name;
  final String surname;
  final String patronymic;
  final String institute;
  final String group;
  final String photo;
  final String sex;
  final String phoneNumber;
  final String country;
  final DateTime birthday;
  final String status;
  final String role;

  const ProfileModel({
    required this.id,
    required this.username,
    required this.email,
    required this.password,
    required this.isActive,
    required this.name,
    required this.surname,
    required this.patronymic,
    required this.institute,
    required this.group,
    required this.photo,
    required this.sex,
    required this.phoneNumber,
    required this.country,
    required this.birthday,
    required this.status,
    required this.role,
  });
}
