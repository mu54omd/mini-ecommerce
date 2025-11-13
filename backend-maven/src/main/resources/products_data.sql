-- ============================================
--  Sample Data for "products" table
--  Based on your Product entity
--  Contains 100 digital tech products
-- ============================================

INSERT INTO products (name, description, price, stock, image_url, created_at, is_active) VALUES
('Apple iPhone 15 Pro', 'Apple flagship smartphone with A17 chip', 999.00, 40, '/uploads/iphone_15_pro.jpg', NOW(), true),
('Apple iPhone 14', 'Apple smartphone with A15 Bionic chip', 799.00, 50, '/uploads/iphone_14.jpg', NOW(), true),
('Apple iPhone 13 Mini', 'Compact iPhone with A15 chip', 699.00, 30, '/uploads/iphone_13_mini.jpg', NOW(), true),
('Samsung Galaxy S24 Ultra', 'Samsung flagship with Snapdragon 8 Gen 3', 1199.00, 35, '/uploads/galaxy_s24_ultra.jpg', NOW(), true),
('Samsung Galaxy Z Fold 5', 'Foldable smartphone with AMOLED screen', 1799.00, 20, '/uploads/galaxy_z_fold_5.jpg', NOW(), true),
('Google Pixel 8 Pro', 'Google flagship phone with Tensor G3', 999.00, 25, '/uploads/pixel_8_pro.jpg', NOW(), true),
('OnePlus 12', 'OnePlus flagship with Snapdragon 8 Gen 3', 899.00, 40, '/uploads/oneplus_12.jpg', NOW(), true),
('Xiaomi 14 Pro', 'High-end Xiaomi phone with Leica camera', 849.00, 50, '/uploads/xiaomi_14_pro.jpg', NOW(), true),
('Sony Xperia 1 V', 'Sony smartphone with 4K OLED screen', 1199.00, 15, '/uploads/xperia_1v.jpg', NOW(), true),
('Huawei P60 Pro', 'Huawei smartphone with great camera', 899.00, 30, '/uploads/huawei_p60_pro.jpg', NOW(), true),

('MacBook Air M2', 'Apple MacBook Air with M2 chip', 1299.00, 30, '/uploads/macbook_air_m2.jpg', NOW(), true),
('MacBook Pro 16 M3', 'Apple MacBook Pro 16-inch with M3 Pro chip', 2499.00, 20, '/uploads/macbook_pro_16_m3.jpg', NOW(), true),
('Dell XPS 15', 'Dell XPS series laptop with Intel i9', 1899.00, 25, '/uploads/dell_xps_15.jpg', NOW(), true),
('Dell G15 5530', 'Dell gaming laptop with RTX 4060', 1399.00, 40, '/uploads/dell_g15_5530.jpg', NOW(), true),
('HP Spectre x360', 'HP premium 2-in-1 ultrabook', 1599.00, 30, '/uploads/hp_spectre_x360.jpg', NOW(), true),
('ASUS ROG Zephyrus G16', 'ASUS gaming laptop with RTX 4080', 2199.00, 20, '/uploads/asus_rog_zephyrus_g16.jpg', NOW(), true),
('Lenovo Legion 7i', 'Lenovo gaming laptop with i9 and RTX 4080', 2399.00, 15, '/uploads/lenovo_legion_7i.jpg', NOW(), true),
('Microsoft Surface Laptop 5', 'Slim Windows ultrabook from Microsoft', 1499.00, 25, '/uploads/surface_laptop_5.jpg', NOW(), true),
('Acer Predator Helios 16', 'Gaming laptop with i7 and RTX 4070', 1799.00, 30, '/uploads/acer_predator_helios_16.jpg', NOW(), true),
('Razer Blade 15', 'Razer gaming laptop with sleek design', 2399.00, 20, '/uploads/razer_blade_15.jpg', NOW(), true),

('iPad Pro 12.9 M2', 'Apple iPad Pro with M2 chip', 1199.00, 50, '/uploads/ipad_pro_m2.jpg', NOW(), true),
('iPad Air M1', 'Apple iPad Air with M1 chip', 699.00, 40, '/uploads/ipad_air_m1.jpg', NOW(), true),
('Samsung Galaxy Tab S9', 'Samsung premium Android tablet', 899.00, 30, '/uploads/galaxy_tab_s9.jpg', NOW(), true),
('Xiaomi Pad 6 Pro', 'High-end Xiaomi tablet', 649.00, 35, '/uploads/xiaomi_pad_6_pro.jpg', NOW(), true),
('Lenovo Tab P12 Pro', 'Android tablet for productivity', 799.00, 25, '/uploads/lenovo_tab_p12_pro.jpg', NOW(), true),

('Apple Watch Ultra 2', 'Rugged smartwatch with GPS and LTE', 799.00, 50, '/uploads/apple_watch_ultra_2.jpg', NOW(), true),
('Apple Watch SE 2', 'Affordable Apple Watch model', 279.00, 60, '/uploads/apple_watch_se_2.jpg', NOW(), true),
('Samsung Galaxy Watch 6', 'Samsung smartwatch with Wear OS', 349.00, 50, '/uploads/galaxy_watch_6.jpg', NOW(), true),
('Xiaomi Smart Band 8', 'Fitness tracker with AMOLED display', 59.00, 100, '/uploads/xiaomi_band_8.jpg', NOW(), true),

('Sony WH-1000XM5', 'Sony noise cancelling headphones', 399.00, 80, '/uploads/sony_wh_1000xm5.jpg', NOW(), true),
('Apple AirPods Pro 2', 'Apple noise cancelling earbuds', 249.00, 100, '/uploads/airpods_pro_2.jpg', NOW(), true),
('Samsung Galaxy Buds 2 Pro', 'Wireless earbuds from Samsung', 199.00, 90, '/uploads/galaxy_buds_2_pro.jpg', NOW(), true),
('Bose QuietComfort Ultra', 'Premium over-ear noise cancelling headphones', 379.00, 40, '/uploads/bose_qc_ultra.jpg', NOW(), true),

('LG UltraGear 27GN950', 'LG 27-inch 4K gaming monitor', 699.00, 25, '/uploads/lg_ultragear_27gn950.jpg', NOW(), true),
('Samsung Odyssey G9', 'Super ultrawide gaming monitor', 1499.00, 15, '/uploads/samsung_odyssey_g9.jpg', NOW(), true),
('ASUS ProArt Display PA32UCX', 'Professional 4K HDR monitor', 2499.00, 10, '/uploads/asus_proart_pa32ucx.jpg', NOW(), true),
('Dell UltraSharp U2723QE', 'Dell 27-inch 4K USB-C monitor', 799.00, 25, '/uploads/dell_ultrasharp_u2723qe.jpg', NOW(), true),

('Canon EOS R8', 'Full-frame mirrorless camera', 1499.00, 20, '/uploads/canon_eos_r8.jpg', NOW(), true),
('Sony A7 IV', 'Sony mirrorless camera with 33MP sensor', 2499.00, 15, '/uploads/sony_a7_iv.jpg', NOW(), true),
('Nikon Z6 II', 'Nikon mirrorless camera', 1999.00, 20, '/uploads/nikon_z6_ii.jpg', NOW(), true),
('Fujifilm X-T5', 'APS-C mirrorless camera from Fujifilm', 1699.00, 25, '/uploads/fujifilm_xt5.jpg', NOW(), true),

('PlayStation 5', 'Sony next-gen gaming console', 499.00, 50, '/uploads/playstation_5.jpg', NOW(), true),
('Xbox Series X', 'Microsoft gaming console', 499.00, 45, '/uploads/xbox_series_x.jpg', NOW(), true),
('Nintendo Switch OLED', 'Nintendo handheld console', 349.00, 60, '/uploads/nintendo_switch_oled.jpg', NOW(), true),
('Steam Deck OLED', 'Valve handheld gaming PC', 599.00, 40, '/uploads/steam_deck_oled.jpg', NOW(), true),

('Logitech MX Master 3S', 'Wireless productivity mouse', 99.00, 100, '/uploads/logitech_mx_master_3s.jpg', NOW(), true),
('Logitech G Pro X Keyboard', 'Mechanical gaming keyboard', 129.00, 80, '/uploads/logitech_g_pro_x.jpg', NOW(), true),
('Razer DeathAdder V3 Pro', 'Wireless gaming mouse', 149.00, 70, '/uploads/razer_deathadder_v3_pro.jpg', NOW(), true),
('Corsair K95 RGB Platinum XT', 'Mechanical keyboard with RGB lighting', 199.00, 50, '/uploads/corsair_k95_xt.jpg', NOW(), true),
('Elgato Stream Deck MK.2', 'Control deck for streamers', 149.00, 40, '/uploads/elgato_stream_deck_mk2.jpg', NOW(), true),

('Apple TV 4K (3rd Gen)', 'Apple TV streaming device', 129.00, 80, '/uploads/apple_tv_4k.jpg', NOW(), true),
('Amazon Fire TV Stick 4K', 'Amazon streaming media stick', 49.00, 100, '/uploads/fire_tv_stick_4k.jpg', NOW(), true),
('Google Chromecast 4K', 'Google Chromecast with Google TV', 59.00, 90, '/uploads/chromecast_4k.jpg', NOW(), true),

('DJI Mini 4 Pro', 'Compact 4K drone', 1099.00, 15, '/uploads/dji_mini_4_pro.jpg', NOW(), true),
('DJI Air 3', 'Mid-range drone with dual camera', 1349.00, 10, '/uploads/dji_air_3.jpg', NOW(), true),
('GoPro Hero 12 Black', 'Action camera with 5.3K video', 399.00, 35, '/uploads/gopro_hero_12.jpg', NOW(), true),
('Insta360 X3', '360° action camera', 449.00, 30, '/uploads/insta360_x3.jpg', NOW(), true),

('Samsung Portable SSD T9', '2TB high-speed external SSD', 199.00, 60, '/uploads/samsung_ssd_t9.jpg', NOW(), true),
('SanDisk Extreme Pro 1TB', 'High-performance portable SSD', 149.00, 70, '/uploads/sandisk_extreme_pro.jpg', NOW(), true),
('WD My Passport 4TB', 'External hard drive USB 3.2', 119.00, 80, '/uploads/wd_my_passport_4tb.jpg', NOW(), true),
('Seagate One Touch 5TB', 'Portable external hard drive', 129.00, 75, '/uploads/seagate_one_touch_5tb.jpg', NOW(), true),

('Anker PowerCore 20000', 'High-capacity power bank', 69.00, 120, '/uploads/anker_powercore_20000.jpg', NOW(), true),
('Anker 737 Charger (GaNPrime)', 'Fast wall charger 120W', 99.00, 100, '/uploads/anker_737_ganprime.jpg', NOW(), true),
('Belkin 3-in-1 MagSafe Stand', 'Wireless charging dock for Apple devices', 149.00, 60, '/uploads/belkin_magsafe_stand.jpg', NOW(), true),
('Baseus USB-C Hub 8-in-1', 'USB-C hub with HDMI and PD', 59.00, 70, '/uploads/baseus_usb_c_hub.jpg', NOW(), true),
('Ugreen Nexode 140W Charger', 'Multiport GaN charger', 129.00, 50, '/uploads/ugreen_nexode_140w.jpg', NOW(), true);
