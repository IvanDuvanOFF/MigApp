import 'package:equatable/equatable.dart';

abstract class MainEvents extends Equatable {}

class OnInit extends MainEvents {
  OnInit();

  @override
  List<Object> get props => [];
}
