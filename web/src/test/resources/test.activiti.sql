insert into ACT_ID_GROUP values ('ad_user', 1, '用户', 'security-role');
insert into ACT_ID_GROUP values ('ad_sb_user', 1, '世巴审核员', 'assignment');
insert into ACT_ID_GROUP values ('ad_bg_user', 1, '北广审核员', 'assignment');

insert into ACT_ID_USER values ('panxh', 1, 'panxh', 'panxh', 'admin@kad.com', '000000', '');
insert into ACT_ID_MEMBERSHIP values ('panxh', 'user');



insert into ACT_ID_USER values ('yqj', 1, 'linda', 'linda1', 'hr@gmail.com', '000000', '');
insert into ACT_ID_MEMBERSHIP values ('yqj', 'ad_user');
insert into ACT_ID_MEMBERSHIP values ('yqj', 'ad_sb_user');

insert into ACT_ID_USER values ('linda', 1, 'linda', 'linda1', 'hr@gmail.com', '000000', '');
insert into ACT_ID_MEMBERSHIP values ('linda', 'ad_user');
insert into ACT_ID_MEMBERSHIP values ('linda', 'ad_sb_user');

insert into ACT_ID_USER values ('guodan', 1, 'guodan', 'guodan1', 'leader@gmail.com', '000000', '');
insert into ACT_ID_MEMBERSHIP values ('guodan', 'ad_user');
insert into ACT_ID_MEMBERSHIP values ('guodan', 'ad_bg_user');
 