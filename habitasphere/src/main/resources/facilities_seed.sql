-- =============================================================================
-- HABITASPHERE FACILITY MANAGEMENT INITIAL SEED DATA
-- =============================================================================
-- Run this script in your PostgreSQL database to seed the default facilities.
-- These inserts use conditional SELECT WHERE NOT EXISTS to avoid duplicate entries.

-- 1. Gym
INSERT INTO facilities (name, description, capacity, is_active)
SELECT 'Gym', 'State-of-the-art fitness center with modern cardio, weightlifting machines, and free weights.', 20, true
WHERE NOT EXISTS (SELECT 1 FROM facilities WHERE name = 'Gym');

-- 2. Club House
INSERT INTO facilities (name, description, capacity, is_active)
SELECT 'Club House', 'Premium community clubhouse featuring lounge areas, meeting spaces, and dining facilities.', 50, true
WHERE NOT EXISTS (SELECT 1 FROM facilities WHERE name = 'Club House');

-- 3. Community Hall
INSERT INTO facilities (name, description, capacity, is_active)
SELECT 'Community Hall', 'Spacious and grand community hall designed for hosting society events, birthday parties, and cultural programs.', 150, true
WHERE NOT EXISTS (SELECT 1 FROM facilities WHERE name = 'Community Hall');

-- 4. Swimming Pool
INSERT INTO facilities (name, description, capacity, is_active)
SELECT 'Swimming Pool', 'Olympic-sized swimming pool along with a designated shallow kids pool area, monitored by lifeguards.', 30, true
WHERE NOT EXISTS (SELECT 1 FROM facilities WHERE name = 'Swimming Pool');

-- 5. Guest Room
INSERT INTO facilities (name, description, capacity, is_active)
SELECT 'Guest Room', 'Comfortably furnished, air-conditioned rooms equipped with basic amenities for residents'' guests.', 4, true
WHERE NOT EXISTS (SELECT 1 FROM facilities WHERE name = 'Guest Room');

-- 6. Indoor Games Room
INSERT INTO facilities (name, description, capacity, is_active)
SELECT 'Indoor Games Room', 'Recreational center containing table tennis tables, billiards/pool tables, foosball, and board games.', 15, true
WHERE NOT EXISTS (SELECT 1 FROM facilities WHERE name = 'Indoor Games Room');
