// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'post_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_PostRequest _$$_PostRequestFromJson(Map<String, dynamic> json) =>
    _$_PostRequest(
      title: json['title'] as String,
      contents: json['contents'] as String,
      img: json['img'] as String,
    );

Map<String, dynamic> _$$_PostRequestToJson(_$_PostRequest instance) =>
    <String, dynamic>{
      'title': instance.title,
      'contents': instance.contents,
      'img': instance.img,
    };
