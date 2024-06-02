create view typographies_and_rest as
(
select distinct t.id,
                t.typography_type,
                t.creation_date,
                t.user_id,
                t.creation_date + d.days - current_date rest
from typographies t
         inner join typography_types tt on t.typography_type = tt.name
         inner join deadlines d on tt.name = d.typography_type
         inner join users u on t.user_id = u.id
where t.status = 'IN_PROGRESS'
  and u.country = d.country
    );