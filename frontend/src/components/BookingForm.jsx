import React, { useState, useEffect } from 'react';
import './BookingForm.css';

const BookingForm = ({ user, appointments, onAddAppointment, onCancelAppointment, onLogout }) => {
  // State để chuyển đổi giữa 2 tab: 'create' (Đặt lịch) và 'history' (Lịch sử)
  const [activeTab, setActiveTab] = useState('create');

  // --- LOGIC FORM ĐẶT LỊCH (Giữ nguyên phần lớn) ---
  const specialties = [
    { id: 'gen', name: 'Đa khoa (General)' },
    { id: 'dent', name: 'Nha khoa (Dentist)' },
    { id: 'card', name: 'Tim mạch (Cardiology)' },
    { id: 'ped', name: 'Nhi khoa (Pediatrics)' }
  ];

  const timeSlots = ['08:00', '08:30', '09:00', '09:30', '10:00', '14:00', '14:30', '15:00'];

  const [formData, setFormData] = useState({
    fullName: user.name || '',
    phone: '',
    specialty: '',
    date: '',
    time: '',
    reason: '',
    age: '',
    gender: 'Nam'
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleTimeSelect = (time) => {
    setFormData({ ...formData, time });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Gọi hàm từ App.jsx để lưu vào dữ liệu chung
    onAddAppointment({
      patient: formData.fullName,
      patientEmail: user.email, // Lưu email để định danh
      phone: formData.phone,
      specialty: formData.specialty,
      date: formData.date,
      time: formData.time,
      reason: formData.reason,
      age: formData.age,
      gender: formData.gender
    });

    alert("Đặt lịch thành công!");
    // Reset form và chuyển sang tab lịch sử để xem kết quả
    setFormData({ ...formData, time: '', reason: '', date: '' });
    setActiveTab('history');
  };

  // --- LOGIC LẤY DANH SÁCH LỊCH HẸN CỦA USER ---
  // Lọc lịch hẹn dựa theo email của người đang đăng nhập
  const myAppointments = appointments.filter(app =>
    app.patientEmail === user.email || app.patient === user.name
  );

  return (
    <div className="booking-container">
      <div className="booking-card">
        {/* Header với nút Logout */}
        <div className="user-header">
           <div>
             <span>Xin chào, <strong>{user.name}</strong></span>
           </div>
           <button onClick={onLogout} className="logout-link">Đăng xuất</button>
        </div>

        {/* TAB NAVIGATION */}
        <div className="tabs">
          <button
            className={`tab-btn ${activeTab === 'create' ? 'active' : ''}`}
            onClick={() => setActiveTab('create')}
          >
            📅 Đặt lịch khám
          </button>
          <button
            className={`tab-btn ${activeTab === 'history' ? 'active' : ''}`}
            onClick={() => setActiveTab('history')}
          >
            📋 Lịch hẹn của tôi
          </button>
        </div>

        {/* NỘI DUNG TAB 1: FORM ĐẶT LỊCH */}
        {activeTab === 'create' && (
          <form onSubmit={handleSubmit} className="booking-form-content">
            <h2>Đăng Ký Khám Bệnh</h2>
            <p className="subtitle">Điền thông tin để lấy số thứ tự nhanh chóng</p>

            <div className="input-row">
              <div className="form-group">
                <label>Họ và tên</label>
                <input type="text" name="fullName" value={formData.fullName} onChange={handleChange} required />
              </div>
              <div className="form-group">
                <label>Tuổi</label>
                <input type="number" name="age" placeholder="VD: 30" value={formData.age} onChange={handleChange} required style={{width: '80px'}} />
              </div>
              <div className="form-group">
                <label>Giới tính</label>
                <select name="gender" value={formData.gender} onChange={handleChange}>
                  <option value="Nam">Nam</option>
                  <option value="Nữ">Nữ</option>
                </select>
              </div>
            </div>

            <div className="input-row">
              <div className="form-group">
                <label>Chuyên khoa</label>
                <select name="specialty" value={formData.specialty} onChange={handleChange} required>
                  <option value="">-- Chọn chuyên khoa --</option>
                  {specialties.map(spec => <option key={spec.id} value={spec.id}>{spec.name}</option>)}
                </select>
              </div>
              <div className="form-group">
                 <label>Ngày khám</label>
                 <input type="date" name="date" value={formData.date} onChange={handleChange} required />
              </div>
            </div>

            <div className="form-group">
              <label>Giờ khám</label>
              <div className="time-slots">
                {timeSlots.map(slot => (
                  <button
                    key={slot} type="button"
                    className={`time-btn ${formData.time === slot ? 'active' : ''}`}
                    onClick={() => handleTimeSelect(slot)}
                  >
                    {slot}
                  </button>
                ))}
              </div>
            </div>

            <div className="form-group">
               <label>Triệu chứng / Lý do khám</label>
               <textarea name="reason" rows="2" value={formData.reason} onChange={handleChange} required placeholder="Mô tả triệu chứng..."></textarea>
            </div>

            <button type="submit" className="submit-btn booking-btn" disabled={!formData.time}>
              Xác Nhận Đặt Lịch
            </button>
          </form>
        )}

        {/* NỘI DUNG TAB 2: DANH SÁCH LỊCH HẸN */}
        {activeTab === 'history' && (
          <div className="history-content">
            <h2>Lịch Hẹn Của Tôi</h2>
            {myAppointments.length === 0 ? (
              <p className="empty-state">Bạn chưa có lịch hẹn nào.</p>
            ) : (
              <div className="appointment-list">
                {myAppointments.map(app => (
                  <div key={app.id} className={`appointment-item status-${app.status}`}>
                    <div className="app-info">
                      <div className="app-time">
                        <span className="time">{app.time}</span>
                        <span className="date">{app.date}</span>
                      </div>
                      <div className="app-details">
                        <strong>{app.reason}</strong>
                        <p className="app-meta">BS Phụ trách: {app.specialty || 'Đa khoa'}</p>
                        <span className={`status-pill pill-${app.status}`}>{app.status}</span>
                      </div>
                    </div>

                    {/* Chỉ cho phép hủy khi trạng thái là PENDING */}
                    {app.status === 'PENDING' && (
                      <button
                        className="cancel-btn"
                        onClick={() => onCancelAppointment(app.id)}
                      >
                        Hủy
                      </button>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

      </div>
    </div>
  );
};

export default BookingForm;