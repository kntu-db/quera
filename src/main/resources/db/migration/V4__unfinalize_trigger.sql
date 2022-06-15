create function unfinalize_submits() returns trigger
    language plpgsql as
$$
begin
    update submit
    set isfinal = false
    where id in (select s.id from submit s where s.problem = new.problem and s.user = new.user and s.isfinal = true);
    return new;
end;
$$;

create trigger unfinalize_submits_trigger
    before insert or update
    on submit
    for each row
    when (pg_trigger_depth() = 0)
execute procedure unfinalize_submits();