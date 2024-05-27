import 'dart:io';
import 'package:deeplom/screens/add_application/add_application_screen.dart';
import 'package:equatable/equatable.dart';

abstract class AddApplicationEvents extends Equatable {
  const AddApplicationEvents();

  @override
  List<Object> get props => [];
}

class OnInit extends AddApplicationEvents {
  const OnInit();

  @override
  List<Object> get props => [];
}

class OnInitSelectedApplication extends AddApplicationEvents {
  const OnInitSelectedApplication();

  @override
  List<Object> get props => [];
}

class PickFileEvent extends AddApplicationEvents {
  final FileSourceType sourceType;
  final String applicationId;
  final String applicationTitle;

  const PickFileEvent(this.sourceType, this.applicationId, this.applicationTitle);

  @override
  List<Object> get props => [sourceType, applicationId, applicationTitle];
}

class UploadFileEvent extends AddApplicationEvents {
  final File file;
  final String applicationId;
  final String applicationTitle;

  const UploadFileEvent(this.file, this.applicationId, this.applicationTitle);

  @override
  List<Object> get props => [file, applicationId, applicationTitle];
}

class AddDocumentEvent extends AddApplicationEvents {
  final String applicationId;
  final String fileName;

  const AddDocumentEvent(this.applicationId, this.fileName);

  @override
  List<Object> get props => [applicationId, fileName];
}

class SelectApplication extends AddApplicationEvents {
  final String applicationId;

  const SelectApplication(this.applicationId);

  @override
  List<Object> get props => [applicationId];
}
