import React, { useState, useEffect } from 'react';
import './BookingForm.css';

const BookingForm = () => {
  // Mock Data: Giả lập dữ liệu từ Backend
  const specialties = [
    { id: 'gen', name: 'Đa khoa (General)' },
    { id: 'dent', name: 'Nha khoa (Dentist)' },
    { id: 'card', name: 'Tim mạch (Cardiology)' },
    { id: 'ped', name: 'Nhi khoa (Pediatrics)' }
  ];

  const doctorsData = {
    gen: [{ id: 1, name: 'BS. Nguyễn Văn A' }, { id: 2, name: 'BS. Trần Thị B' }],
    dent: [{ id: 3, name: 'BS. Lê Văn C' }],
    card: [{ id: 4, name: 'ThS. Phạm Minh D' }, { id: 5, name: 'GS. Ngô Bảo E' }],
    ped: [{ id: 6, name: 'BS. Hoàng Yến' }]
  };

  const timeSlots = [
    '08:00', '08:30', '09:00', '09:30', '10:00', '10:30',
    '13:30', '14:00', '14:30', '15:00', '15:30', '16:00'
  ];

  // State
  const [formData, setFormData] = useState({
    fullName: '',
    phone: '',
    specialty: '',
    doctorId: '',
    date: '',
    time: '',
    reason: ''
  });

  const [availableDoctors, setAvailableDoctors] = useState([]);
  const [isSubmitted, setIsSubmitted] = useState(false);

  // Effect: Khi chọn chuyên khoa, cập nhật danh sách bác sĩ
  useEffect(() => {
    if (formData.specialty) {
      setAvailableDoctors(doctorsData[formData.specialty] || []);
      // Reset bác sĩ nếu đổi chuyên khoa
      setFormData(prev => ({ ...prev, doctorId: '' }));
    } else {
      setAvailableDoctors([]);
    }
  }, [formData.specialty]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleTimeSelect = (time) => {
    setFormData({ ...formData, time });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Booking Data:', formData);
    // Giả lập gọi API thành công
    setTimeout(() => {
      setIsSubmitted(true);
    }, 1000);
  };

  // Màn hình thông báo thành công
  if (isSubmitted) {
    return (
      <div className="booking-container">
        <div className="booking-card success-card">
          <div className="success-icon">✅</div>
          <h2>Đặt lịch thành công!</h2>
          <p>Cảm ơn <strong>{formData.fullName}</strong>.</p>
          <p>Lịch hẹn với <strong>{availableDoctors.find(d => d.id == formData.doctorId)?.name}</strong> lúc <strong>{formData.time}</strong> ngày <strong>{formData.date}</strong> đã được ghi nhận.</p>
          <button className="reset-btn" onClick={() => setIsSubmitted(false)}>Đặt lịch mới</button>
        </div>
      </div>
    );
  }

  return (
    <div className="booking-container">
      <div className="booking-card">
        <h2>Đặt Lịch Khám Bệnh</h2>
        <p className="subtitle">Điền thông tin để đăng ký lịch khám nhanh chóng</p>

        <form onSubmit={handleSubmit}>
          {/* Thông tin cá nhân */}
          <div className="form-section">
            <h3>1. Thông tin bệnh nhân</h3>
            <div className="input-row">
              <div className="form-group">
                <label>Họ và tên</label>
                <input
                  type="text" name="fullName" placeholder="VD: Nguyễn Văn A"
                  value={formData.fullName} onChange={handleChange} required
                />
              </div>
              <div className="form-group">
                <label>Số điện thoại</label>
                <input
                  type="tel" name="phone" placeholder="09xxxxxxxxx"
                  value={formData.phone} onChange={handleChange} required
                />
              </div>
            </div>
          </div>

          {/* Chọn dịch vụ */}
          <div className="form-section">
            <h3>2. Thông tin khám</h3>
            <div className="input-row">
              <div className="form-group">
                <label>Chuyên khoa</label>
                <select name="specialty" value={formData.specialty} onChange={handleChange} required>
                  <option value="">-- Chọn chuyên khoa --</option>
                  {specialties.map(spec => (
                    <option key={spec.id} value={spec.id}>{spec.name}</option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>Bác sĩ (Tùy chọn)</label>
                <select name="doctorId" value={formData.doctorId} onChange={handleChange} disabled={!formData.specialty}>
                  <option value="">-- Chọn bác sĩ --</option>
                  {availableDoctors.map(doc => (
                    <option key={doc.id} value={doc.id}>{doc.name}</option>
                  ))}
                </select>
              </div>
            </div>
          </div>

          {/* Chọn thời gian */}
          <div className="form-section">
            <h3>3. Thời gian</h3>
            <div className="form-group">
              <label>Ngày khám</label>
              <input
                type="date" name="date"
                value={formData.date} onChange={handleChange} required
              />
            </div>

            <div className="form-group">
              <label>Giờ khám khả dụng</label>
              <div className="time-slots">
                {timeSlots.map(slot => (
                  <button
                    key={slot}
                    type="button"
                    className={`time-btn ${formData.time === slot ? 'active' : ''}`}
                    onClick={() => handleTimeSelect(slot)}
                  >
                    {slot}
                  </button>
                ))}
              </div>
              <input type="hidden" name="time" value={formData.time} required />
              {/* Input ẩn để validate required HTML5 nếu cần, hoặc check thủ công */}
            </div>
          </div>

          <div className="form-group">
             <label>Triệu chứng / Ghi chú</label>
             <textarea
               name="reason" rows="3"
               placeholder="Mô tả sơ qua tình trạng sức khỏe..."
               value={formData.reason} onChange={handleChange}
             ></textarea>
          </div>

          <button type="submit" className="submit-btn booking-btn" disabled={!formData.time}>
            Xác Nhận Đặt Lịch
          </button>
        </form>
      </div>
    </div>
  );
};

export default BookingForm;