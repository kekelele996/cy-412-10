CREATE DATABASE IF NOT EXISTS smartestate_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smartestate_db;
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  phone VARCHAR(32) NOT NULL UNIQUE,
  password_hash VARCHAR(160) NOT NULL,
  nickname VARCHAR(64) NOT NULL,
  avatar VARCHAR(255) DEFAULT NULL,
  role ENUM('resident','co-resident','staff','admin') NOT NULL DEFAULT 'resident',
  building VARCHAR(32) DEFAULT NULL,
  unit VARCHAR(32) DEFAULT NULL,
  room VARCHAR(32) DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_users_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS repairs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  title VARCHAR(120) NOT NULL,
  description TEXT NOT NULL,
  type ENUM('water_power','furniture','public_facility','other') NOT NULL DEFAULT 'other',
  images TEXT DEFAULT NULL,
  status ENUM('pending','assigned','processing','done','closed') NOT NULL DEFAULT 'pending',
  handler_id BIGINT DEFAULT NULL,
  rating INT DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_repairs_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_repairs_handler FOREIGN KEY (handler_id) REFERENCES users(id),
  INDEX idx_repairs_status (status),
  INDEX idx_repairs_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS payments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  fee_type ENUM('property','parking','utilities') NOT NULL DEFAULT 'property',
  amount DECIMAL(10,2) NOT NULL,
  month VARCHAR(7) NOT NULL,
  status ENUM('unpaid','paid','overdue') NOT NULL DEFAULT 'unpaid',
  paid_at DATETIME DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_payments_user FOREIGN KEY (user_id) REFERENCES users(id),
  INDEX idx_payments_user_month (user_id, month),
  INDEX idx_payments_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS announcements (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(160) NOT NULL,
  content TEXT NOT NULL,
  category ENUM('notice','event','urgent') NOT NULL DEFAULT 'notice',
  publisher_id BIGINT NOT NULL,
  publish_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  top TINYINT(1) NOT NULL DEFAULT 0,
  read_count INT NOT NULL DEFAULT 0,
  CONSTRAINT fk_announcements_publisher FOREIGN KEY (publisher_id) REFERENCES users(id),
  INDEX idx_announcements_top_publish (top, publish_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS announcement_reads (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  announcement_id BIGINT NOT NULL,
  read_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_reads_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_reads_announcement FOREIGN KEY (announcement_id) REFERENCES announcements(id),
  UNIQUE KEY uk_user_announcement (user_id, announcement_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(32) NOT NULL UNIQUE,
  name VARCHAR(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS permissions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(80) NOT NULL UNIQUE,
  name VARCHAR(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS role_permissions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  role_code VARCHAR(32) NOT NULL,
  permission_code VARCHAR(80) NOT NULL,
  UNIQUE KEY uk_role_permission (role_code, permission_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS house_members (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  building VARCHAR(32) NOT NULL,
  unit VARCHAR(32) NOT NULL,
  room VARCHAR(32) NOT NULL,
  user_id BIGINT NOT NULL,
  relation ENUM('owner','co-resident') NOT NULL DEFAULT 'co-resident',
  invited_by BIGINT DEFAULT NULL,
  status ENUM('active','removed') NOT NULL DEFAULT 'active',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_house_members_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_house_members_inviter FOREIGN KEY (invited_by) REFERENCES users(id),
  INDEX idx_house_members_house (building, unit, room),
  INDEX idx_house_members_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS operation_logs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT DEFAULT NULL,
  role VARCHAR(32) DEFAULT NULL,
  action VARCHAR(80) NOT NULL,
  entity_name VARCHAR(80) NOT NULL,
  entity_id BIGINT DEFAULT NULL,
  message VARCHAR(500) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_operation_logs_action (action),
  INDEX idx_operation_logs_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO roles(code, name) VALUES
('resident', '业主'),
('co-resident', '同住人'),
('staff', '物业'),
('admin', '管理员')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO permissions(code, name) VALUES
('dashboard:view', '查看工作台'),
('repair:view', '查看报修'),
('repair:create', '提交报修'),
('repair:assign', '分配报修'),
('repair:update', '更新报修'),
('payment:view', '查看费用'),
('payment:pay', '模拟缴费'),
('announcement:view', '查看公告'),
('announcement:publish', '发布公告'),
('user:profile', '编辑个人资料'),
('operationLog:view', '查看操作日志'),
('household:view', '查看住户'),
('household:manage', '管理同住人')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO role_permissions(role_code, permission_code) VALUES
('resident','dashboard:view'),
('resident','repair:view'),
('resident','repair:create'),
('resident','payment:view'),
('resident','payment:pay'),
('resident','announcement:view'),
('resident','user:profile'),
('resident','household:view'),
('resident','household:manage'),
('co-resident','dashboard:view'),
('co-resident','repair:view'),
('co-resident','repair:create'),
('co-resident','payment:view'),
('co-resident','announcement:view'),
('co-resident','user:profile'),
('co-resident','household:view'),
('staff','dashboard:view'),
('staff','repair:view'),
('staff','repair:assign'),
('staff','repair:update'),
('staff','payment:view'),
('staff','announcement:view'),
('staff','announcement:publish'),
('staff','user:profile'),
('staff','household:view'),
('admin','dashboard:view'),
('admin','repair:view'),
('admin','repair:create'),
('admin','repair:assign'),
('admin','repair:update'),
('admin','payment:view'),
('admin','payment:pay'),
('admin','announcement:view'),
('admin','announcement:publish'),
('admin','user:profile'),
('admin','operationLog:view'),
('admin','household:view'),
('admin','household:manage')
ON DUPLICATE KEY UPDATE permission_code = VALUES(permission_code);

INSERT INTO users(id, phone, password_hash, nickname, avatar, role, building, unit, room) VALUES
(1, '13800000001', '1000:demo-admin-salt:74d2c1e9f73', '林经理', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200', 'admin', '物业中心', 'A', '001'),
(2, '13800000002', '1000:demo-staff-salt:4e6f8a2121a', '周管家', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200', 'staff', '物业中心', 'B', '002'),
(3, '13800000003', '1000:demo-resident-salt:8d9e3c6b2a1', '陈业主', 'https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=200', 'resident', '8栋', '2单元', '1201'),
(4, '13800000004', '1000:demo-coresident-salt:a1b2c3d4e5f', '王同住', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200', 'co-resident', '8栋', '2单元', '1201')
ON DUPLICATE KEY UPDATE nickname = VALUES(nickname), role = VALUES(role);

INSERT INTO repairs(id, user_id, title, description, type, images, status, handler_id, rating) VALUES
(1, 3, '厨房水槽下方渗水', '夜间用水后柜体底部积水，需要尽快检查管线。', 'water_power', '', 'processing', 2, NULL),
(2, 3, '单元门禁无法识别', '门禁刷卡成功但门锁不弹开，影响晚归通行。', 'public_facility', '', 'assigned', 2, NULL),
(3, 3, '客厅吊灯闪烁', '灯具频闪，疑似线路接触不良。', 'water_power', '', 'pending', NULL, NULL)
ON DUPLICATE KEY UPDATE title = VALUES(title), status = VALUES(status);

INSERT INTO payments(id, user_id, fee_type, amount, month, status, paid_at) VALUES
(1, 3, 'property', 426.00, '2026-06', 'unpaid', NULL),
(2, 3, 'parking', 280.00, '2026-06', 'paid', '2026-06-03 10:20:00'),
(3, 3, 'utilities', 168.50, '2026-05', 'paid', '2026-05-28 18:00:00')
ON DUPLICATE KEY UPDATE amount = VALUES(amount), status = VALUES(status);

INSERT INTO announcements(id, title, content, category, publisher_id, publish_at, top, read_count) VALUES
(1, '暴雨天气地下车库巡检安排', '6月15日晚间物业将加强排水设备巡检，请车主留意车库广播。', 'urgent', 2, '2026-06-15 09:30:00', 1, 42),
(2, '端午社区便民服务开放预约', '本周六开放家电清洗、磨刀、义诊服务，业主可在物业前台预约。', 'event', 2, '2026-06-12 14:00:00', 0, 128),
(3, '6月公共区域消杀通知', '6月18日9:00-11:30进行楼道及地库消杀，请提前收好门口物品。', 'notice', 2, '2026-06-10 08:40:00', 0, 87)
ON DUPLICATE KEY UPDATE title = VALUES(title), top = VALUES(top);

INSERT INTO house_members(building, unit, room, user_id, relation, invited_by, status) VALUES
('8栋', '2单元', '1201', 3, 'owner', NULL, 'active'),
('8栋', '2单元', '1201', 4, 'co-resident', 3, 'active')
ON DUPLICATE KEY UPDATE status = VALUES(status);
