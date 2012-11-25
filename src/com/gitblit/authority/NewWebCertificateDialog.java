/*
 * Copyright 2012 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gitblit.authority;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.gitblit.client.HeaderPanel;
import com.gitblit.client.Translation;
import com.gitblit.utils.StringUtils;
import com.toedter.calendar.JDateChooser;

public class NewWebCertificateDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	JDateChooser expirationDate;
	JTextField hostname;
	boolean isCanceled = true;

	public NewWebCertificateDialog(Frame owner, Date defaultExpiration) {
		super(owner);
		
		setTitle(Translation.get("gb.newWebCertificate"));
		
		JPanel content = new JPanel(new BorderLayout(Utils.MARGIN, Utils.MARGIN)) {			
			private static final long serialVersionUID = 1L;

			@Override
			public Insets getInsets() {
				
				return Utils.INSETS;
			}
		};
		content.add(new HeaderPanel(Translation.get("gb.newWebCertificate"), "rosette_16x16.png"), BorderLayout.NORTH);
		
		expirationDate = new JDateChooser(defaultExpiration);
		hostname = new JTextField(20);
		
		JPanel panel = new JPanel(new GridLayout(0, 2, Utils.MARGIN, Utils.MARGIN));
		
		panel.add(new JLabel(Translation.get("gb.hostname")));
		panel.add(hostname);

		panel.add(new JLabel(Translation.get("gb.expires")));
		panel.add(expirationDate);

		content.add(panel, BorderLayout.CENTER);
		
		JButton ok = new JButton(Translation.get("gb.ok"));
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateInputs()) {
					isCanceled = false;
					setVisible(false);
				}
			}
		});
		JButton cancel = new JButton(Translation.get("gb.cancel"));
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isCanceled = true;
				setVisible(false);
			}
		});
		
		JPanel controls = new JPanel();
		controls.add(ok);
		controls.add(cancel);
		
		content.add(controls, BorderLayout.SOUTH);
		
		getContentPane().add(content, BorderLayout.CENTER);
		pack();
		
		setLocationRelativeTo(owner);
	}
	
	private boolean validateInputs() {
		if (getExpiration().getTime() < System.currentTimeMillis()) {
			// expires before now
			JOptionPane.showMessageDialog(this, Translation.get("gb.invalidExpirationDate"),
					Translation.get("gb.error"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (StringUtils.isEmpty(getHostname())) {
			// must have hostname
			JOptionPane.showMessageDialog(this, Translation.get("gb.hostnameRequired"),
					Translation.get("gb.error"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	public String getHostname() {
		return hostname.getText();
	}
	
	public Date getExpiration() {
		return expirationDate.getDate();
	}
	
	public boolean isCanceled() {
		return isCanceled;
	}
}
