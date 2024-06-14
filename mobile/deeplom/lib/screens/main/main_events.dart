import 'package:equatable/equatable.dart';

abstract class MainEvents extends Equatable {}

class OnInit extends MainEvents {
  OnInit();

  @override
  List<Object> get props => [];
}

class GetApplicationByDate extends MainEvents {
  GetApplicationByDate({required this.date});
  final String date;

  @override
  List<Object> get props => [date];
}

class GetAllApplication extends MainEvents {
  GetAllApplication();

  @override
  List<Object> get props => [];
}
