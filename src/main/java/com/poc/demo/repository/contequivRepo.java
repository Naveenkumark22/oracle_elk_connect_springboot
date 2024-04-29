package com.poc.demo.repository;

import com.poc.demo.model.contequiv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface contequivRepo extends JpaRepository<contequiv,Number> {

   @Query(value=" SELECT   admin_client_id,description,END_DT  FROM contequiv  WHERE    description='SK Internal' and end_dt is null and rownum<=1000 ",nativeQuery = true)
  //  @Query(value="select * from contequiv  where  description='MAINGK'  and rownum<=100 ",nativeQuery = true)
    List<contequiv> getkeys( );


    @Query(value=" SELECT admin_client_id,description,END_DT FROM (SELECT admin_client_id,description,END_DT, ROWNUM AS rnum FROM contequiv  WHERE   description='SK Internal' and end_dt is null and ROWNUM <= 2000) WHERE description='SK Internal' and end_dt is null and  rnum > 1000 ",nativeQuery = true)
        //  @Query(value="select * from contequiv  where  description='MAINGK'  and rownum<=100 ",nativeQuery = true)
    List<contequiv> getkeys1();


   // @Query(value=" SELECT   admin_client_id,description,END_DT  FROM contequiv  WHERE    description='SK Internal' and end_dt is null and rownum<=10000 ",nativeQuery = true)
   // Page<contequiv> findAll1(Pageable pageable);


    @Query(value=" SELECT   admin_client_id,description,END_DT  FROM contequiv  WHERE    description='SK Internal' and end_dt is null  ",nativeQuery = true)
    Page<contequiv> findAll11( Pageable pageable);





 @Query(value = "SELECT admin_client_id,description,END_DT FROM (SELECT admin_client_id,description,END_DT, ROWNUM as rn FROM contequiv  WHERE    description='SK Internal' and end_dt is null) WHERE description='SK Internal' and end_dt is null and  rn > ?1 AND rn <= ?2",
         nativeQuery = true)
 List<contequiv> findWithCustomQuery(int startRow, int endRow);

 @Query(value = "select count(*) FROM contequiv WHERE description='SK Internal' and end_dt is null",nativeQuery = true)
 Long getCount();

}


