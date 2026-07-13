USE zzyl;
ALTER TABLE sys_prepaid ADD COLUMN elder_id_card VARCHAR(20);
ALTER TABLE sys_prepaid ADD COLUMN bed_no VARCHAR(50);
ALTER TABLE sys_elder_balance ADD COLUMN elder_id_card VARCHAR(20);
ALTER TABLE sys_elder_balance ADD COLUMN bed_no VARCHAR(50);
