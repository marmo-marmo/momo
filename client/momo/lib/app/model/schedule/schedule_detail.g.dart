// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'schedule_detail.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_ScheduleDetail _$$_ScheduleDetailFromJson(Map<String, dynamic> json) =>
    _$_ScheduleDetail(
      id: json['id'] as int,
      title: json['title'] as String,
      contents: json['contents'] as String,
      authorNickname: json['authorNickname'] as String,
      authorImage: json['authorImage'] as String,
      startDateTime: json['startDateTime'] as String,
      offline: json['offline'] as bool,
      attendance: json['attendance'] as bool,
      attendanceCheck: json['attendanceCheck'] as bool,
    );

Map<String, dynamic> _$$_ScheduleDetailToJson(_$_ScheduleDetail instance) =>
    <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'contents': instance.contents,
      'authorNickname': instance.authorNickname,
      'authorImage': instance.authorImage,
      'startDateTime': instance.startDateTime,
      'offline': instance.offline,
      'attendance': instance.attendance,
      'attendanceCheck': instance.attendanceCheck,
    };
