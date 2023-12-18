package com.office.library.book.admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.MallBookVo;
import com.office.library.book.RentalBookVo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataAccessException;

@Component
public class BookDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SqlSession sqlSession;
	
	public boolean isISBN(String b_isbn) {
		String sql =  "SELECT COUNT(*) FROM tbl_book "
					+ "WHERE b_isbn = ?";
		int result = jdbcTemplate.queryForObject(sql, Integer.class, b_isbn);
		return result > 0 ? true : false;
	}
	
	public int insertBook(BookVo bookVo) {
		String sql = "INSERT INTO tbl_book(";
		sql += " b_thumbnail, ";
		sql += " b_name, ";
		sql += " b_author, ";
		sql += " b_publisher, ";
		sql += " b_publish_year, ";
		sql += " b_isbn, ";
		sql += " b_call_number, ";
		sql += " b_rental_able, ";
		sql += " b_reg_date, b_mod_date)";
		sql += " VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
		
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql,
											bookVo.getB_thumbnail(), 
											bookVo.getB_name(),
											bookVo.getB_author(),
											bookVo.getB_publisher(),
											bookVo.getB_publish_year(),
											bookVo.getB_isbn(),
											bookVo.getB_call_number(),
											bookVo.getB_rental_able()
											);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public List<BookVo> selectBooksBySearch(BookVo bookVo) {
		
		String sql =  "SELECT * FROM tbl_book "
					+ "WHERE b_name LIKE ? "
					+ "ORDER BY b_no DESC";
		
		List<BookVo> bookVos = null;
		
		try {
			RowMapper<BookVo> rowMapper = BeanPropertyRowMapper.newInstance(BookVo.class);
			bookVos = jdbcTemplate.query(sql, rowMapper, "%" + bookVo.getB_name() + "%");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return bookVos.size() > 0 ? bookVos : null;
		
	}
	
	public BookVo selectBook(int b_no) {
		String sql = "SELECT * FROM tbl_book WHERE b_no = ?";
		List<BookVo> bookVos = null;
		
		try {
			RowMapper<BookVo> rowMapper = BeanPropertyRowMapper.newInstance(BookVo.class);
			bookVos = jdbcTemplate.query(sql, rowMapper, b_no);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return bookVos.size() > 0 ? bookVos.get(0) : null;
		
	}
	
	
	public int updateBook(BookVo bookVo) {
		
		List<String> args = new ArrayList<String>();
		
		String sql =  "UPDATE tbl_book SET ";
			   if (bookVo.getB_thumbnail() != null) {
				   sql += "b_thumbnail = ?, ";
				   args.add(bookVo.getB_thumbnail());
			   }
			   
			   sql += "b_name = ?, ";
			   args.add(bookVo.getB_name());
			   
			   sql += "b_author = ?, ";
			   args.add(bookVo.getB_author());
			   
			   sql += "b_publisher = ?, ";
			   args.add(bookVo.getB_publisher());
			   
			   sql += "b_publish_year = ?, ";
			   args.add(bookVo.getB_publish_year());
			   
			   sql += "b_isbn = ?, ";
			   args.add(bookVo.getB_isbn());
			   
			   sql += "b_call_number = ?, ";
			   args.add(bookVo.getB_call_number());
			   
			   sql += "b_rental_able = ?, ";
			   args.add(Integer.toString(bookVo.getB_rental_able()));
			   
			   sql += "b_mod_date = NOW() ";
			   
			   sql += "WHERE b_no = ?";
			   args.add(Integer.toString(bookVo.getB_no()));
		
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql, args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int deleteBook(int b_no) {
		String sql =  "DELETE FROM tbl_book "
					+ "WHERE b_no = ?";
		int result = -1;
		try {
			result = jdbcTemplate.update(sql, b_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<BookVo> selectAllBooks() {
		String sql =  "SELECT * FROM tbl_book "
					+ "ORDER BY b_reg_date DESC";
		List<BookVo> books = new ArrayList<BookVo>();
		try {
			
			RowMapper<BookVo> rowMapper = BeanPropertyRowMapper.newInstance(BookVo.class);
			books = jdbcTemplate.query(sql, rowMapper);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return books.size() > 0 ? books : null;
		
	}
	
	
	public List<HopeBookVo> selectHopeBooks() {
		String sql = "SELECT * FROM tbl_hope_book hb "
				+ "JOIN tbl_user_member um "
				+ "ON hb.u_m_no = um.u_m_no "
				+ "ORDER BY hb_no DESC";
		List<HopeBookVo> hopeBookVos = new ArrayList<HopeBookVo>();
		try {
			RowMapper<HopeBookVo> rowMapper = BeanPropertyRowMapper.newInstance(HopeBookVo.class);
			hopeBookVos = jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hopeBookVos;
	}
	
	public void updateHopeBookResult(int hb_no) {
		String sql = "UPDATE tbl_hope_book SET hb_result=1, hb_result_last_date=NOW() WHERE hb_no = ?";
		
		try {
			jdbcTemplate.update(sql, hb_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public List<RentalBookVo> selectRentalBooks() {
		String sql =  "SELECT * FROM tbl_rental_book rb "
					+ "JOIN tbl_book b "
					+ "ON rb.b_no = b.b_no "
					+ "JOIN tbl_user_member um "
					+ "ON rb.u_m_no = um.u_m_no "
					+ "WHERE rb.rb_end_date = '1000-01-01' "
					+ "ORDER BY um.u_m_id ASC, rb.rb_reg_date DESC";
		
		List<RentalBookVo> rentalBookVos = new ArrayList<RentalBookVo>();
		
		try {
			
			RowMapper<RentalBookVo> rowMapper = BeanPropertyRowMapper.newInstance(RentalBookVo.class);
			rentalBookVos = jdbcTemplate.query(sql, rowMapper);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return rentalBookVos;
		
	}
	
	public int updateRentalBook(int rb_no) {
		String sql =  "UPDATE tbl_rental_book "
					+ "SET rb_end_date = NOW() "
					+ "WHERE rb_no = ?";
		
		int result = -1;
		
		try {
			
			result = jdbcTemplate.update(sql, rb_no);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return result;
	}
	
	public int updateBook(int b_no) {
		
		String sql =  "UPDATE tbl_book "
					+ "SET b_rental_able = 1 "
					+ "WHERE b_no = ?";
		
		int result = -1;
		
		try {
			
			result = jdbcTemplate.update(sql, b_no);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return result;
		
	}
	
	public int bookMallConfirm(MallBookVo mallBookVo) throws DataAccessException {
		int result = -1;
		result = sqlSession.insert("mapper.book.bookMallInsert", mallBookVo);
		return result;
	}
	
}
