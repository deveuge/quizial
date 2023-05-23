CREATE VIEW vw_quiz_favorites AS
SELECT q.id AS id, COUNT(f.quiz_id) AS fav_count
	FROM quiz q JOIN favorite f ON q.id = f.quiz_id
    GROUP by q.id;